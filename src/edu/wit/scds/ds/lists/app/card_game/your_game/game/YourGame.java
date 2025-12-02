/* @formatter:off
 *
 * © David M Rosenberg
 *
 * Topic: List App ~ Card Game
 *
 * Usage restrictions:
 *
 * You may use this code for exploration, experimentation, and furthering your
 * learning for this course. You may not use this code for any other
 * assignments, in my course or elsewhere, without explicit permission, in
 * advance, from myself (and the instructor of any other course).
 *
 * Further, you may not post (including in a public repository such as on github)
 * nor otherwise share this code with anyone other than current students in my
 * sections of this course.
 *
 * Violation of these usage restrictions will be considered a violation of
 * Wentworth Institute of Technology's Academic Honesty Policy.  Unauthorized posting
 * or use of this code may also be considered copyright infringement and may subject
 * the poster and/or the owners/operators of said websites to legal and/or financial
 * penalties.  My students are permitted to store this code in a private repository
 * or other private cloud-based storage.
 *
 * Do not modify or remove this notice.
 *
 * @formatter:on
 */


package edu.wit.scds.ds.lists.app.card_game.your_game.game ;

import edu.wit.scds.ds.lists.app.card_game.standard_cards.card.Card ;
import edu.wit.scds.ds.lists.app.card_game.standard_cards.card.Rank ;
import edu.wit.scds.ds.lists.app.card_game.standard_cards.card.Suit ;
import edu.wit.scds.ds.lists.app.card_game.standard_cards.pile.Deck ;
import edu.wit.scds.ds.lists.app.card_game.standard_cards.pile.Pile ;
import edu.wit.scds.ds.lists.app.card_game.your_game.pile.DiscardPile ;
import edu.wit.scds.ds.lists.app.card_game.your_game.pile.Meld ;
import edu.wit.scds.ds.lists.app.card_game.your_game.pile.Stock ;
import edu.wit.scds.ds.lists.app.card_game.universal_base.card.CardBase;
import edu.wit.scds.ds.lists.app.card_game.universal_base.support.NoCardsException;

import static edu.wit.scds.ds.lists.app.card_game.standard_cards.card.Rank.JOKER ;

import java.util.ArrayList ;
import java.util.Collections ;
import java.util.LinkedList ;
import java.util.List ;
import java.util.ListIterator ;
import java.util.Scanner ;


// you will modify this code - talk to me if you have any questions

/**
 * The main driver for the game of Rummy. It supports 3-5 players.
 * Players take turns using a simple character cell console interface.
 * <p>
 * Goal: to be the first to reach 100 points or force others to exceed it.
 * <p>
 * Rules:
 * <ul>
 * <li>Use 1 deck for 3-4 players, 2 decks for 5 players.
 * <li>Players are dealt 7 cards (3-4 players) or 6 cards (5 players).
 * <li>Players draw from Stock or Discard, Meld valid sets/runs, and Discard.
 * <li>Players can lay off cards on existing melds.
 * <li>The round ends when a player empties their hand.
 * </ul>
 *
 * @author David M Rosenberg
 *
 * @version 1.0 2025-03-27 Initial implementation
 * @version 2.0 2025-06-28 track changes to other classes
 * @version 2.1 2025-11-19 validate the deck(s) at the end of the game
 *
 * @author Jason Mansour
 * @author Michael Foley
 *
 * @version 3.0 2025-11-30 modifications for Rummy implementation
 */
public final class YourGame
    {

    /*
     * constants
     */


    /** can't play with fewer than this many decks at an absolute minimum */
    private final static int MINIMUM_NUMBER_OF_DECKS = 1 ;

    /** can't play with fewer than this many players at an absolute minimum */
    private final static int MINIMUM_PLAYER_COUNT = 3 ;
    
    /** Score required to win the game */
    private static final int WINNING_SCORE = 100;


    /*
     * data fields
     */


    private final List<Player> players ;
    private int numberOfPlayers ;

    private int numberOfCardsPerHand ;
    private int numberOfRounds ;    // Used as scoring threshold container
    private int numberOfDecks ;

    private int roundNumber ;

    private final Scanner playerInput ;

    private final List<Deck> decks ;
    private final Stock stock ;
    private final DiscardPile discardPile ;

    private boolean running = false ;


    /*
     * constructors
     */


    /**
     * set up the game instance
     *
     * @param input
     * used for player interactions
     */
    private YourGame( final Scanner input )
        {

        this.running = false ;

        this.players = new ArrayList<>() ;  // indexing is O(1)
        this.numberOfPlayers = -1 ;

        this.numberOfCardsPerHand = -1 ;
        this.numberOfRounds = -1 ;

        this.roundNumber = 0 ;

        this.playerInput = input ;

        this.stock = new Stock() ;

        this.discardPile = new DiscardPile() ;


        this.numberOfDecks = -1 ;

        this.decks = new ArrayList<>() ;   // indexing is O(1)

        }   // end constructor


    /*
     * game driver
     */


    /**
     * This is the top-level driver for the game of Rummy.
     *
     * @param args
     * -unused-
     */
    public static void main( final String[] args )
        {

        try ( final Scanner input = new Scanner( System.in ) ; )
            {
            final YourGame yourGame = new YourGame( input ) ;

            welcome() ;

            displayDivider() ;

            yourGame.setup() ;
            
            // Start the main game loop which handles rounds until someone wins
            yourGame.play();

            yourGame.tearDown() ;
            }   // end try (input)

        }   // end main()


    /*
     * operational methods
     */
    
    /**
     * Main game loop that manages rounds and checks for a winner
     */
    private void play() {
        int dealerIndex = 0;
        
        while (this.running) {
            // Check for Game Winner
            for (Player p : players) {
                if (p.getScore() >= WINNING_SCORE) {
                    System.out.printf("%nGAME OVER! %s has won with %d points!%n", p.name, p.getScore());
                    this.running = false;
                    return;
                }
            }

            run(); 
            
            // Check if user quit during run()
            if (!this.running) return;

            displayDivider();
            summary(); // Show scores
            displayDivider();

            final String playAgain = promptForLine("Start next round? (Y/N)");
            if (playAgain == null || Character.toLowerCase(playAgain.charAt(0)) != 'y') {
                this.running = false;
                return;
            }

            reset();
            
            // Rotate dealer
            dealerIndex = (dealerIndex + 1) % numberOfPlayers;
        }
    }


    /**
     * determine the number of decks, create them, and populate the stock from
     * them
     *
     * @since 2.0
     */
    private void configureCards()
        {
        // Rummy Rule: 2 decks for 5 players, 1 deck otherwise
        if ( this.numberOfPlayers == 5 ) 
            {
            this.numberOfDecks = 2;
            }
        else 
            {
            this.numberOfDecks = 1;
            }
            
        System.out.printf( "%nUsing %,d deck(s) for %,d players.%n", this.numberOfDecks, this.numberOfPlayers );

        // open the appropriate number of decks (no jokers) and put the cards
        // into the stock
        getCardsFromDecks() ;

        // shuffle the cards
        this.stock.shuffle() ;

        }   // end configureCards()


    /**
     * determine the number of cards for each hand
     *
     * @since 2.0
     */
    private void configureCardsPerHand()
        {
        // Rummy Rule: 6 cards for 5 players, 7 cards otherwise
        if ( this.numberOfPlayers == 5 )
            {
            this.numberOfCardsPerHand = 6;
            }
        else
            {
            this.numberOfCardsPerHand = 7;
            }
            
        System.out.printf( "Dealing %,d cards to each player.%n", this.numberOfCardsPerHand );

        }   // end configureCardsPerHand()


    /**
     * determine the number of rounds to play
     *
     * @since 2.0
     */
    private void configureNumberOfRounds()
        {
        // Rummy is played to a score, not a fixed number of rounds.
        System.out.println( "First player to reach 100 points wins!" );
        // We set this just to follow the template structure
        this.numberOfRounds = WINNING_SCORE; 

        }   // end configureNumberOfRounds()


    /**
     * determine the number of players and set up for play
     *
     * @since 2.0
     */
    private void configurePlayers()
        {

        // find out how many players

        do
            {
            this.numberOfPlayers = promptForInt( "How many players (3-5)?", MINIMUM_PLAYER_COUNT ) ;

            if ( !this.running )
                {
                return ;
                }
            
            if ( this.numberOfPlayers < MINIMUM_PLAYER_COUNT )
                {
                System.out.printf( "That is not enough players. Rummy requires at least %d.%n", MINIMUM_PLAYER_COUNT ) ;
                }
            else if ( this.numberOfPlayers > 5 )
                {
                System.out.printf( "That is too many players. The maximum is 5.%n" ) ;
                }
            }
        while ( this.numberOfPlayers < MINIMUM_PLAYER_COUNT || this.numberOfPlayers > 5  ) ;

        // create the players
        

        for ( int i = 1 ; i <= this.numberOfPlayers ; i++ )
            {
            final String playerName =
                    promptForLine( String.format( "%nWhat is the name of player %,d?", i ) ) ;

            if ( !this.running )
                {
                return ;
                }

            this.players.add( new Player( playerName ) ) ;
            }

        }   // end configurePlayers()


    /**
     * deal hands to all players
     *
     * @since 2.0
     */
    private void dealHands()
        {

        // deal one card to each player in turn
        for ( int i = 1 ; i <= this.numberOfCardsPerHand ; i++ )
            {

            for ( final Player aPlayer : this.players )
                {
                final Card dealt = this.stock.drawTopCard().hide() ;
                aPlayer.dealtACard( dealt ) ;
                }

            }
            
        // Rummy: Flip top card to discard pile to start
        try {
            this.discardPile.addCard(this.stock.drawTopCard().reveal());
        } catch (NoCardsException e) {
            // Should not happen on initial deal
        }

        }   // end dealHands()


    /**
     * display a visual separator between sections of output
     *
     * @since 2.0
     */
    private static void displayDivider()
        {

        System.out.printf( "%n--------------------%n%n" ) ;

        }   // end displayDivider()


    /**
     * display the current standings for each of the players
     *
     * @since 2.0
     */
    private void displayStandings()
        {

        System.out.printf( "%nCurrent Scores:%n" ) ;

        for ( final Player aPlayer : this.players )
            {
            System.out.printf( "\t%s: %d points%n", aPlayer.name, aPlayer.getScore() ) ;
            }

        }   // end displayStandings()


    /**
     * populate stock from all playing cards (excludes jokers) from one or more
     * decks
     */
    private void getCardsFromDecks()
        {

        // populate the stock from the requisite number of decks

        final Card joker = new Card( JOKER ) ;    // for lookup

        for ( int i = 1 ; i <= this.numberOfDecks ; i++ )
            {
            // 'open' a 'box' of cards
            final Deck newDeck = new Deck() ;

            // take the cards out of the box
            final Pile newCards = newDeck.removeAllCards() ;

            // pull out the jokers, turn them face up and put them back in the
            // 'box'
            newDeck.moveCardsToBottom( newCards.removeAllMatchingCards( joker ).revealAll() ) ;

            // add this set of cards to the stock
            this.stock.moveCardsToBottom( newCards ) ;

            // save the 'box'
            this.decks.add( newDeck ) ;
            }

        }   // end getCardsFromDecks()


    /**
     * prepare the game to run again
     */
    private void reset()

        {

        this.stock.moveCardsToBottom( this.discardPile ) ;

        for ( final Player aPlayer : this.players )
            {
            this.stock.moveCardsToBottom( aPlayer.turnInAllCards() ) ;
            }

        this.stock.shuffle() ;

        }   // end reset()


    /**
     * primary driver for the game (plays one single round)
     */
    private void run()
        {
        // deal initial hands
        dealHands() ;

        int currentPlayerIndex = 0;
        boolean roundOver = false;

        // Loop until someone goes out
        while (!roundOver && this.running)
            {
            Player currentPlayer = players.get(currentPlayerIndex);
            
            // --- TURN LOGIC ---
            
            // 1. Display Game State
            System.out.printf("%n--- %s's Turn ---%n", currentPlayer.name);
            displayHandWithIndices(currentPlayer);

            // 2. The draw portion
            Card drawnCard = null;
            boolean validDraw = false;
            
            // fixed so now if you put something other than s or d its an error
            while (!validDraw && this.running) {
                String drawChoice = promptForLine("Draw from (S)tock or (D)iscard?");
                if (!this.running) return;
                
                if (drawChoice.equalsIgnoreCase("S")) {
                    // now this is where all the stock logic is 
                    if (stock.isEmpty()) {
                        // Reshuffle discard into stock if empty
                        if (discardPile.isEmpty()) {
                            System.out.println("Draw game: No cards left.");
                            roundOver = true;
                            validDraw = true; // Break loop to exit round
                            break;
                        }
                        Card topDiscard = discardPile.takeTopCard();
                        stock.moveCardsToBottom(discardPile); // Move rest
                        stock.shuffle();
                        discardPile.addCard(topDiscard); // Put top back
                        System.out.println("Stock replenished from discard pile.");
                    }
                    if (!stock.isEmpty()) {
                        drawnCard = stock.drawTopCard().reveal();
                        validDraw = true;
                    }
                } else if (drawChoice.equalsIgnoreCase("D")) {
                    // right here is the discard logic
                    if (discardPile.isEmpty()) {
                        System.out.println("Discard pile is empty. You must draw from Stock.");
                    } else {
                        drawnCard = discardPile.takeTopCard(); // Already revealed
                        validDraw = true;
                    }
                } else {
                    // INVALID INPUT
                    System.out.println("Invalid selection. Please type 'S' or 'D'.");
                }
            }
            
            // Check if round ended due to no cards
            if (roundOver) break;
            
            System.out.println("You drew: " + drawnCard);
            currentPlayer.dealtACard(drawnCard);
            
            // 3. The melding portion
            boolean melding = true;
            while (melding && !currentPlayer.isHandEmpty() && this.running) {
                // Refresh display after draw/meld to show updated hand
                displayHandWithIndices(currentPlayer);
                System.out.println("Meld Options:");
                System.out.println(" - Enter indices for NEW meld (ex. '0 1 2')");
                System.out.println(" - Enter 'add' character and meld index to ADD to existing meld (ex. 'a 4 0')");
                System.out.println(" - Enter 'pass' to finish");
                
                String meldInput = promptForLine("Choice:");
                if (!this.running) return;
                
                if (meldInput.equalsIgnoreCase("pass") || meldInput.isEmpty()) {
                    melding = false;
                } 
                // bug fix: making this so you can actually add to existing meld 
                else if (meldInput.toLowerCase().startsWith("a")) {
                    try {
                        String[] parts = meldInput.split(" ");
                        int cardIdx = Integer.parseInt(parts[1]);
                        int meldIdx = Integer.parseInt(parts[2]);
                        
                        List<Meld> tableMelds = getAllMeldsOnTable();
                        
                        if (meldIdx >= 0 && meldIdx < tableMelds.size() && 
                            cardIdx >= 0 && cardIdx < currentPlayer.getHand().cardCount()) {
                            
                            Card cardToPlay = currentPlayer.getCardAt(cardIdx);
                            Meld targetMeld = tableMelds.get(meldIdx);
                            
                            if (canAddToMeld(cardToPlay, targetMeld)) {
                                // Move the card
                                targetMeld.addToBottom(currentPlayer.playCardAt(cardIdx));
                                if (((Card) targetMeld.getCardAt(0)).rank != ((Card) targetMeld.getCardAt(1)).rank) {
                                    targetMeld.sort(); 
                                }
                                System.out.println("Card added to meld!");
                            } else {
                                System.out.println("Invalid move: Card does not fit that meld.");
                            }
                        } else {
                            System.out.println("Invalid indices.");
                        }
                    } catch (Exception e) {
                        System.out.println("Invalid format. Use 'a <card> <meld>'");
                    }
                } 
                // this portion is for making a new meld that is not existing
                else {
                    List<Integer> indices = parseIndices(meldInput, currentPlayer.getHand().cardCount());
                    if (indices.size() >= 3) {
                        // Extract candidate cards to validate
                        List<Card> candidates = new ArrayList<>();
                        // Sort indices descending so we can validate before removing
                        indices.sort(Collections.reverseOrder());
                        
                        // Peek at cards
                        for (int idx : indices) {
                            candidates.add(currentPlayer.getCardAt(idx));
                        }
                        
                        if (isValidMeld(candidates)) {
                            Meld newMeld = new Meld();
                            for (int idx : indices) {
                                newMeld.addToBottom(currentPlayer.playCardAt(idx));
                            }
                            currentPlayer.addMeld(newMeld);
                            System.out.println("Meld placed successfully!");
                        } else {
                            System.out.println("Invalid Meld! Must be a Set (same rank) or Run (same suit, sequence).");
                        }
                    } else {
                        System.out.println("Invalid input. Must select at least 3 cards.");
                    }
                }
            }
            
            // 4. Phase where you can discard 
            // have to make this so it doesn't end until pass
            if (!currentPlayer.isHandEmpty() && this.running) {
                displayHandWithIndices(currentPlayer);
                int discardIdx = -1;
                while (discardIdx < 0 || discardIdx >= currentPlayer.getHand().cardCount()) {
                    discardIdx = promptForInt("Enter index of card to discard:");
                    if (!this.running) return;
                }
                
                Card discarded = currentPlayer.playCardAt(discardIdx);
                discardPile.addCard(discarded.reveal());
                System.out.println("Discarded " + discarded);
            }
            
            // 5. final win condition
            if (currentPlayer.isHandEmpty()) {
                System.out.printf("%n%s went out! Round Over.%n", currentPlayer.name);
                calculateRoundScores(currentPlayer);
                roundOver = true;
            }
            
            // Next player
            currentPlayerIndex = (currentPlayerIndex + 1) % numberOfPlayers;
            } // End round loop

        }   // end run()


    /**
     * prepare to play the game
     */
    private void setup()
        {
        System.out.println("Welcome to Rummy");
        // we're not set up yet but we're on our way
        this.running = true ;   // input methods will set this false based upon
                                // user input


        // configure the game components

        configurePlayers() ;

        if ( !this.running )
            {
            return ;
            }

        configureCards() ;

        if ( !this.running )
            {
            return ;
            }

        configureCardsPerHand() ;

        if ( !this.running )
            {
            return ;
            }

        configureNumberOfRounds() ;

        // we'll begin game play if still running

        }   // end setup()


    /**
     * displays the results of playing the game
     */
    private void summary()
        {
        System.out.printf( "End of Game Summary%n" ) ;
        displayStandings();
        }   // end summary()


    /**
     * finished running the game
     */
    private void tearDown()
        {

        displayDivider() ;

        // release most resources
        reset() ;

        this.players.clear() ;

        // return the cards to the decks (put them back in their boxes)
        this.stock.sort() ; // the cards are all in the stock

        // assertion: 'same' cards are grouped next to each other

        // whether we have the right number of cards or not, re-box them
        int deckIndex = 0 ;

        while ( !this.stock.isEmpty() )
            {
            // remove the top card from the stock, turn it face up (so we'll be
            // able to display
            // them), add it to the 'next' deck

            this.decks.get( deckIndex ).addToBottom( this.stock.removeTopCard() ) ;

            // move to the 'next' deck
            deckIndex = ( deckIndex + 1 ) % this.decks.size() ;
            }

        // validate the decks
        for ( final Deck deck : this.decks )
            {
            deck.validateDeck() ;
            }

        // free up the decks
        this.decks.clear() ;

        System.out.printf( "%n%nThank you for playing Rummy!%n%n" ) ;

        }   // end tearDown()


    /**
     * display introductory message
     *
     * @since 2.0
     */
    private static void welcome()
        {

        System.out.printf( """

                           Welcome to Rummy!

                           In this game, players collect melds (Sets or Runs).
                           First player to 100 points wins!

                           Respond to any prompt with a period to end the game.

                           Enjoy!
                           """ ) ;

        }   // end welcome()


    /*
     * utility methods
     */
    
    // rummy specific
    
    // Updated helper to show Table Melds with indices
    private void displayHandWithIndices( final Player p )
        {
        // Show Melds on Table
        System.out.println( "--- Melds on Table ---" ) ;
        List<Meld> allMelds = getAllMeldsOnTable();
        
        if (allMelds.isEmpty()) {
            System.out.println( "  [None]" ) ;
        } else {
            for (int i = 0; i < allMelds.size(); i++) {
                Meld m = allMelds.get(i);
                // Reveal all to ensure visibility
                m.revealAll();
                String s = m.toString(); 
                // Clean brackets for display if needed
                if(s.length() > 2) s = s.substring(1, s.length()-1);
                System.out.printf("  [%d] %s%n", i, s);
            }
        }

        // Show Discard Pile
        System.out.printf( "%nDiscard Pile Top: %s%n",
                           this.discardPile.isEmpty()
                                   ? "[Empty]"
                                   : this.discardPile.getTopCard() ) ;

        // Show Player's Hand
        System.out.println( "Your Hand:" ) ;
        p.getHand().revealAll() ;
        int i = 0 ;
        for ( final CardBase c : p.getHand() )
            {
            System.out.printf( "[%d] %s  ", i++, c ) ;
            }
        System.out.println() ;

        }   // end displayHandWithIndices()
    
    /**
     * Flattens all players' melds into one list so we can index them (0, 1, 2...)
     */
    private List<Meld> getAllMeldsOnTable() {
        List<Meld> allMelds = new ArrayList<>();
        // Assuming Player has a method to get their melds. 
        // We need to access the private list, so we added getMelds() to Player.java
        for (Player p : players) {
            allMelds.addAll(p.getMelds());
        }
        return allMelds;
    }
    
    /**
     * Checks if a card can be added to an existing meld.
     *
     * @param card
     * the card attempting to be added
     * @param meld
     * the target meld
     * @return true if the card fits the meld's rules (Set or Run), false otherwise
     */
    private boolean canAddToMeld( Card card,
                                  Meld meld )
        {
        if ( meld.isEmpty() )
            {
            return false ;
            }

        // Get a snapshot of the meld to analyze
        List<Card> cards = new ArrayList<>() ;
        for ( CardBase c : meld )
            {
            cards.add( (Card) c ) ;
            }

        // Check if it's a SET which happens if all ranks matches
        boolean isSet = true ;
        Rank firstRank = cards.get( 0 ).rank ;
        for ( Card c : cards )
            {
            if ( c.rank != firstRank )
                {
                isSet = false ;
                }
            }

        if ( isSet )
            {
            // To add to a set, rank must match
            return card.rank == firstRank ;
            }

        // If not a Set then it has to be a run
        // Sort existing run to find ends
        cards.sort( ( c1,
                      c2 ) -> Integer.compare( c1.rank.getOrder(), c2.rank.getOrder() ) ) ;

        Suit runSuit = cards.get( 0 ).suit ;

        // Must match suit
        if ( card.suit != runSuit )
            {
            return false ;
            }

        // Must be exactly one lower than min, or one higher than max
        int minOrder = cards.get( 0 ).rank.getOrder() ;
        int maxOrder = cards.get( cards.size() - 1 ).rank.getOrder() ;
        int cardOrder = card.rank.getOrder() ;

        return ( cardOrder == minOrder - 1 ) || ( cardOrder == maxOrder + 1 ) ;

        }
    
    private List<Integer> parseIndices(String input, int max) {
        List<Integer> list = new ArrayList<>();
        try {
            String[] parts = input.split(" ");
            for (String s : parts) {
                if (!s.isEmpty()) {
                    int val = Integer.parseInt(s);
                    if (val >= 0 && val < max && !list.contains(val)) list.add(val);
                }
            }
        } catch (NumberFormatException e) {
            return new ArrayList<>(); // Return empty on bad input
        }
        return list;
    }
    
    private boolean isValidMeld(List<Card> cards) {
        // Check Set (All ranks same)
        boolean isSet = true;
        Rank r = cards.get(0).rank;
        for (Card c : cards) if (c.rank != r) isSet = false;
        if (isSet) return true;

        // Check Run (Same Suit, Sequential)
        Suit s = cards.get(0).suit;
        for (Card c : cards) if (c.suit != s) return false;
        
        // Check sequence (Using Order)
        List<Card> sorted = new ArrayList<>(cards);
        // Sort by Rank Order (Ace=1 ... King=13)
        sorted.sort((c1, c2) -> Integer.compare(c1.rank.getOrder(), c2.rank.getOrder()));
        
        for (int i = 0; i < sorted.size() - 1; i++) {
            if (sorted.get(i).rank.getOrder() + 1 != sorted.get(i+1).rank.getOrder()) return false;
        }
        return true;
    }
    
    private void calculateRoundScores(Player winner) {
        int points = 0;
        for (Player p : players) {
            if (p != winner) {
                int penalty = p.calculateHandPoints();
                System.out.printf("%s has %d points left in hand.%n", p.name, penalty);
                points += penalty;
            }
        }
        winner.addScore(points);
        System.out.printf("%s wins the round and gets %d points!%n", winner.name, points);
    }


    /**
     * displays a formatted prompt
     *
     * @param prompt
     * the prompt with optional formatting specifiers
     * @param arguments
     * argument(s) used by the formatting specifiers
     */
    private static void displayPrompt( final String prompt,
                                       final Object... arguments )
        {

        System.out.printf( "%s ", String.format( prompt, arguments ) ) ;

        }   // end displayPrompt()


    /**
     * prompt the user for a card by specifying suit and rank
     * <p>
     * Note: by default, this card is temporary and should only be used for
     * lookups/comparisons, not added to the current set of playing cards
     *
     * @param prompt
     * the prompt with optional formatting specifiers
     * @param arguments
     * argument(s) used by the formatting specifiers
     *
     * @return a card as specified by the user or null if no more input is
     * available or the user requested to exit
     */
    private Card promptForCard( final String prompt, final Object... arguments )
        {

        Suit suit = null ;
        Rank rank = null ;

        do
            {
            String input ;

            displayPrompt( prompt, arguments ) ;

            input = null ;

            // end if no input available
            if ( !this.playerInput.hasNext() )
                {
                this.running = false ;

                return null ;
                }

            // get a line, remove all whitespace, convert to uppercase
            input = this.playerInput.nextLine().replace( " ", "" ).replace( "\t", "" ).toUpperCase() ;

            // no problem if no input, try again
            if ( input.length() == 0 )
                {
                continue ;
                }

            // valid specifications are exactly 1 or 2 characters
            if ( input.length() > 2 )
                {
                System.out.printf( "%nValid responses must have 1 or 2 characters, please try again" ) ;

                continue ;
                }

            // valid 1-character inputs:
            // - 'R' for Joker
            // - '?' display help then re-prompt
            // - '.' to exit
            if ( input.length() == 1 )
                {

                if ( ".".equals( input ) )  // quit
                    {
                    this.running = false ;

                    return null ;
                    }

                if ( "?".equals( input ) )  // help
                    {
                    Rank.displayHelp() ;
                    Suit.displayHelp() ;

                    continue ;
                    }

                if ( "R".equals( input ) )  // JOKER
                    {
                    rank = Rank.JOKER ;
                    suit = Suit.NA ;

                    break ;
                    }

                }

            // assertion: input has 2 characters

            // valid specification is RS where R is the rank and S is the suit
            final String rankElement = input.substring( 0, 1 ) ;
            final String suitElement = input.substring( 1, 2 ) ;

            // if either is '.', exit
            if ( ".".equals( rankElement ) || ".".equals( suitElement ) )
                {
                this.running = false ;

                return null ;
                }

            // either or both might return null
            rank = Rank.interpretDescription( rankElement ) ;
            suit = Suit.interpretDescription( suitElement ) ;
            }
        while ( ( rank == null ) || ( suit == null ) ) ;

        // assertion: we have a rank and a suit

        return new Card( rank, suit ) ;

        }   // end promptForCard()


    /**
     * prompts the user for a positive integer value greater than 0
     *
     * @param prompt
     * the prompt with optional formatting specifiers
     * @param arguments
     * argument(s) used by the formatting specifiers
     *
     * @return the integer value as specified by the user or -1 if no more input
     * is available
     */
    private int promptForInt( final String prompt,
                              final Object... arguments )
        {

        do
            {
            displayPrompt( prompt, arguments ) ;

            if ( this.playerInput.hasNextInt() )    // have an int?
                {
                final int inputValue = this.playerInput.nextInt() ;

                if ( inputValue >= 0 )   // have an int, make sure it's positive
                    {
                    // clear out anything left in the scanner's buffer on the
                    // current line
                    this.playerInput.nextLine() ;

                    return inputValue ;
                    }

                }

            if ( !this.playerInput.hasNext() )  // no more input available?
                {
                this.running = false ;

                return -1 ;
                }

            // assertion: there's more input available but the next token isn't
            // an int

            if ( ".".equals( this.playerInput.next() ) )    // skip the noise
                {
                this.running = false ;

                return -1 ;
                }

            }
        while ( true ) ;    // try again

        }   // end promptForInt()


    /**
     * prompts the user for a line of text
     *
     * @param prompt
     * the prompt with optional formatting specifiers
     * @param arguments
     * argument(s) used by the formatting specifiers
     *
     * @return the non-empty line of text as specified by the user with leading
     * and trailing whitespace removed or null if no more input available
     */
    private String promptForLine( final String prompt,
                                  final Object... arguments )
        {

        String response = "" ;
        String compressedResponse = "" ;

        do
            {
            displayPrompt( prompt, arguments ) ;

            if ( !this.playerInput.hasNextLine() )  // no more input available?
                {
                this.running = false ;

                return null ;
                }

            // get the line
            response = this.playerInput.nextLine().trim() ;

            // make sure we got something other than whitespace
            compressedResponse = response.replace( " ", "" ).replace( "\t", "" ) ;

            // quit?
            if ( ".".equals( compressedResponse ) )
                {
                this.running = false ;

                return null ;
                }

            }
        while ( "".equals( compressedResponse ) ) ;

        // assertion: we have the user's trimmed input (no leading or trailing
        // whitespace

        return response ;

        }   // end promptForLine()

    }   // end class YourGame