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


package edu.wit.scds.ds.lists.app.card_game.top_this.game ;

import edu.wit.scds.ds.lists.app.card_game.standard_cards.card.Card ;
import edu.wit.scds.ds.lists.app.card_game.standard_cards.card.Rank ;
import edu.wit.scds.ds.lists.app.card_game.standard_cards.card.Suit ;
import edu.wit.scds.ds.lists.app.card_game.standard_cards.pile.Deck ;
import edu.wit.scds.ds.lists.app.card_game.standard_cards.pile.Pile ;
import edu.wit.scds.ds.lists.app.card_game.top_this.pile.DiscardPile ;
import edu.wit.scds.ds.lists.app.card_game.top_this.pile.Stock ;

import static edu.wit.scds.ds.lists.app.card_game.standard_cards.card.Rank.JOKER ;

import java.util.ArrayList ;
import java.util.LinkedList ;
import java.util.List ;
import java.util.ListIterator ;
import java.util.Scanner ;


/**
 * NOTE This is a sample, fictitious card game
 * <p>
 * This is the main driver for the game of Top This. It supports 3 or more
 * players. Players take turns using a simple character cell console interface.
 * <p>
 * Goal: to collect the most melds
 * <p>
 * Rules:
 * <ul>
 * <li>use 1 or more decks of standard playing cards (52 cards per deck, 4
 * suits, 13 ranks, no jokers) as specified by the players
 * <li>3 or more players are each dealt the same number of cards (as specified
 * by the players), one at a time, in rotation
 * <li>the players specify the number of rounds
 * <li>the first player dealt a card is the first to play in the first round
 * <li>in subsequent rounds, the first player is the second player from the
 * previous round
 * <li>a round is
 * <ul>
 * <li>each player selects one card from their hand and in turn places it face
 * down on the table
 * <li>once all players have played a card, the played cards are turned face up
 * <li>if there is a single highest card, whichever player played it takes all
 * the cards and saves them as a meld
 * <li>if two or more players played the highest card, there is no winner for
 * that round and the cards are placed face down on the discard pile
 * </ul>
 * <li>
 * <li>a player wins the round when their card is the highest played in that
 * round
 * <li>if there is no winner in a round (2 or more players have the highest
 * card), the cards are discarded
 * <li>the cards won during a round are given to the player with the highest
 * card and saved as a meld
 * <li>the game ends when the specified number of rounds have been played
 * <li>whichever player has the most melds at the end of the game wins
 * </ul>
 *
 * @author David M Rosenberg
 *
 * @version 1.0 2025-03-27 Initial implementation
 * @version 2.0 2025-06-28 track changes to other classes
 * @version 2.1 2025-11-19 validate the deck(s) at the end of the game
 */
public final class TopThis
    {

    /*
     * constants
     */


    /** can't play with fewer than this many decks at an absolute minimum */
    private final static int MINIMUM_NUMBER_OF_DECKS = 1 ;

    /** can't play with fewer than this many players at an absolute minimum */
    private final static int MINIMUM_PLAYER_COUNT = 3 ;


    /*
     * data fields
     */


    private final List<Player> players ;
    private int numberOfPlayers ;

    private int numberOfCardsPerHand ;
    private int numberOfRounds ;    // can't exceed numberOfCardsPerHand
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
     *     used for player interactions
     */
    private TopThis( final Scanner input )
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
     * This is the top-level driver for the game of Top This.
     *
     * @param args
     *     -unused-
     */
    public static void main( final String[] args )
        {

        try ( final Scanner input = new Scanner( System.in ) ; )
            {
            final TopThis topThis = new TopThis( input ) ;

            welcome() ;

            displayDivider() ;

            topThis.setup() ;

            while ( topThis.running )
                {
                topThis.run() ;

                if ( !topThis.running )
                    {
                    topThis.tearDown() ;

                    return ;
                    }

                displayDivider() ;

                topThis.summary() ;

                displayDivider() ;

                final String playAgain = topThis.promptForLine( "Play again?" ) ;

                if ( Character.toLowerCase( playAgain.charAt( 0 ) ) != 'y' )
                    {
                    topThis.running = false ;

                    topThis.tearDown() ;

                    return ;
                    }

                topThis.reset() ;
                }

            topThis.tearDown() ;
            }   // end try (input)

        }   // end main()


    /*
     * operational methods
     */


    /**
     * determine the number of decks, create them, and populate the stock from
     * them
     *
     * @since 2.0
     */
    private void configureCards()
        {

        // assertion: players have already been configured

        do
            {
            this.numberOfDecks =
                    promptForInt( "%nHow many decks (minimum %,d)?",
                                  Math.max( ( this.numberOfPlayers / Deck.CARD_COUNT_IN_A_DECK ) + 1,
                                            MINIMUM_NUMBER_OF_DECKS ) ) ;

            if ( !this.running )
                {
                return ;
                }

            }
        while ( this.numberOfDecks < MINIMUM_NUMBER_OF_DECKS ) ;

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

        // get the number of cards per hand

        final int maximumCardsPerHand = this.stock.cardCount() / this.numberOfPlayers ;

        do
            {
            this.numberOfCardsPerHand = promptForInt( "%nHow many cards per hand (minimum %,d, maximum %,d)?",
                                                      1,
                                                      maximumCardsPerHand ) ;

            if ( !this.running )
                {
                return ;
                }

            }
        while ( this.numberOfCardsPerHand > maximumCardsPerHand ) ;

        }   // end configureCardsPerHand()


    /**
     * determine the number of rounds to play
     *
     * @since 2.0
     */
    private void configureNumberOfRounds()
        {

        // get the number of rounds to play

        do
            {
            this.numberOfRounds = promptForInt( "%nHow many rounds (minimum %,d, maximum %,d)?",
                                                1,
                                                this.numberOfCardsPerHand ) ;

            if ( !this.running )
                {
                return ;
                }

            }
        // can't play more rounds than there are cards in a player's hand
        while ( this.numberOfRounds > this.numberOfCardsPerHand ) ;

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
            this.numberOfPlayers = promptForInt( "How many players (minimum %,d)?", MINIMUM_PLAYER_COUNT ) ;

            if ( !this.running )
                {
                return ;
                }

            }
        while ( this.numberOfPlayers < MINIMUM_PLAYER_COUNT ) ;

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

        System.out.printf( "%nAt the end of round %,d of %,d, the standings are:%n",
                           this.roundNumber,
                           this.numberOfRounds ) ;

        for ( final Player aPlayer : this.players )
            {
            final int meldCount = aPlayer.getMeldCount() ;
            System.out.printf( "\t%s: %s meld%s%n",
                               aPlayer.name,
                               meldCount == 0
                                       ? "no"
                                       : String.format( "%,d", meldCount ),
                               meldCount == 1
                                       ? ""
                                       : "s" ) ;
            }

        }   // end displayStandings()


    /**
     * populate stock from all playing cards (excludes jokers) from one or more
     * decks
     */
    private void getCardsFromDecks()
        {

        // populate the stock from the requisite number of decks

        // NOTE we're determining the maximum # of cards per player based on the
        // # of cards in all
        // the decks
        // NOTE alternative: determine minimum # of decks based upon the # of
        // hands and # of
        // cards/hand

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

        // assertion: each deck in this.decks have all cards that won't be used
        // during game play

        // assertion: this.stock contains all cards to be used during game play

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
     * primary driver for the game
     */
    private void run()
        {

        this.running = true ;

        displayDivider() ;

        System.out.printf( """
                           To specify a card, type RS (Rank and Suit) then press enter.

                           If R or S is ?, we'll display the options for Rank or Suit, respectively.
                           If your selection is ?, we'll display the options for Rank and Suit.

                           If R or S is ., the game will end.
                           If your selection is ., the game will end.

                           Have fun!
                           """ ) ;


        // deal initial hands
        dealHands() ;

        // assertion: all players have the same number of cards in their hand

        int firstPlayerThisRound = 0 ;

        final List<Player> highCardHolders = new LinkedList<>() ;

        // take turns playing
        for ( this.roundNumber = 1 ; this.roundNumber <= this.numberOfRounds ; this.roundNumber++ )
            {

            if ( !this.running )
                {
                return ;
                }

            // make new pile to hold the cards played during this round
            // cards will be added face down
            // after all players have taken their turn, the cards will all be
            // turned face up
            final Pile cardsInPlay = new Pile() {}.setDefaultFaceDown() ;


            displayDivider() ;

            System.out.printf( "Round %,d of %,d%n", this.roundNumber, this.numberOfRounds ) ;

            // (re-)set high card tracking
            Card highCard = null ;
            highCardHolders.clear() ;

            for ( int i = 0 ; i < this.numberOfPlayers ; i++ )
                {
                final int currentPlayerIndex = ( firstPlayerThisRound + i ) % this.numberOfPlayers ;
                final Player currentPlayer = this.players.get( currentPlayerIndex ) ;

                System.out.printf( "%nIt's %s's turn%n", currentPlayer.name ) ;

                Card cardToPlay = null ;

                while ( cardToPlay == null )
                    {
                    cardToPlay = promptForCard( "%nChoose a card from %s: ", currentPlayer.revealHand() ) ;

                    if ( !this.running )
                        {
                        this.stock.moveCardsToBottom( cardsInPlay ) ;

                        return ;
                        }

                    // cardToPlay is null if the specified card isn't in the
                    // player's hand
                    cardToPlay = currentPlayer.playACard( cardToPlay ) ;
                    }

                cardsInPlay.addToBottom( cardToPlay ) ;


                // NOTE the determination of highest card and winner(s) should
                // follow card selection
                // by all players but this is simpler for demonstration purposes

                // is this the highest card so far?
                if ( highCardHolders.isEmpty() )
                    {
                    // this is the first card so it's highest
                    highCard = cardToPlay ;
                    highCardHolders.add( currentPlayer ) ;
                    }
                else
                    {
                    // we already have at least 1 highest card

                    // we care about rank and suit when comparing cards
                    final int cardComparison = cardToPlay.compareTo( highCard ) ;

                    if ( cardComparison > 0 )
                        {
                        // new high card
                        highCard = cardToPlay ;
                        highCardHolders.clear() ;
                        highCardHolders.add( currentPlayer ) ;
                        }
                    else if ( cardComparison == 0 )
                        {
                        // duplicate of high card
                        highCardHolders.add( currentPlayer ) ;
                        }

                    // otherwise, card is lower than the highest
                    }

                }   // end for

            // reveal all the cards that were played this round
            cardsInPlay.revealAll() ;

            displayDivider() ;

            final int highCardHolderCount = highCardHolders.size() ;

            if ( highCardHolderCount == 1 )
                {
                // we have a solo winner of this round

                final Player winner = highCardHolders.removeFirst() ;

                System.out.printf( "%s won round %,d with the highest card %s of %s%n",
                                   winner.name,
                                   this.roundNumber,
                                   highCard,
                                   cardsInPlay ) ;

                // give the winner the cards
                winner.wonRound( cardsInPlay ) ;
                }
            else
                {
                // multiple winners
                final StringBuilder highCardHolderNames = new StringBuilder() ;
                String delimiter = "" ;

                final ListIterator<Player> winnerIterator = highCardHolders.listIterator() ;

                while ( winnerIterator.hasNext() )
                    {
                    final Player aWinner = winnerIterator.next() ;

                    highCardHolderNames.append( delimiter ).append( aWinner.name ) ;

                    winnerIterator.remove() ;

                    if ( highCardHolders.size() == 1 )
                        {
                        delimiter = " and " ;
                        }
                    else
                        {
                        delimiter = ", " ;
                        }

                    }

                // 2 or more of high card - no winner of this round
                System.out.printf( "No one won round %,d; %,d player%s, %s, had the highest card %s of %s%n",
                                   this.roundNumber,
                                   highCardHolderCount,
                                   ( highCardHolderCount == 1
                                           ? ""
                                           : "s" ),
                                   highCardHolderNames.toString(),
                                   highCard,
                                   cardsInPlay ) ;

                // discard the cards
                this.discardPile.moveCardsToTop( cardsInPlay ) ;
                }

            displayStandings() ;

            // start the next round with the next player
            firstPlayerThisRound++ ;
            }   // end for(roundNumber)

        }   // end run()


    /**
     * prepare to play the game
     */
    private void setup()
        {

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

        System.out.printf( "End of game summary%n" ) ;

        int highestMeldCount = 0 ;
        final List<Player> winners = new LinkedList<>() ;

        for ( final Player aPlayer : this.players )
            {
            System.out.printf( "%s%n", aPlayer ) ;

            final int playerMeldCount = aPlayer.getMeldCount() ;

            if ( playerMeldCount == 0 )
                {
                continue ;
                }

            if ( playerMeldCount > highestMeldCount )
                {
                // new highest meld count
                highestMeldCount = playerMeldCount ;

                winners.clear() ;
                winners.add( aPlayer ) ;
                }
            else if ( playerMeldCount == highestMeldCount )
                {
                // duplicate highest meld count
                winners.add( aPlayer ) ;
                }
            // otherwise, aPlayer's meld count is less than the highest we've
            // already seen

            }

        final int numberOfWinners = winners.size() ;

        if ( numberOfWinners == 0 )
            {
            // no winners
            System.out.printf( "%nThere was no winner%n" ) ;
            }
        else if ( numberOfWinners == 1 )
            {
            // solo winner
            System.out.printf( "%n%s won with %,d meld%s%n",
                               winners.getFirst().name,
                               highestMeldCount,
                               ( 1 == highestMeldCount
                                       ? ""
                                       : "s" ) ) ;
            }
        else
            {
            // multiple winners
            final StringBuilder winnersNames = new StringBuilder() ;
            String delimiter = "" ;

            final ListIterator<Player> winnerIterator = winners.listIterator() ;

            while ( winnerIterator.hasNext() )
                {
                final Player aWinner = winnerIterator.next() ;

                winnersNames.append( delimiter ).append( aWinner.name ) ;

                winnerIterator.remove() ;

                if ( winners.size() == 1 )
                    {
                    delimiter = " and " ;
                    }
                else
                    {
                    delimiter = ", " ;
                    }

                }

            System.out.printf( "%nWe have a tie! The winners are %s with %,d meld%s each%n",
                               winnersNames.toString(),
                               highestMeldCount,
                               ( highestMeldCount == 1
                                       ? ""
                                       : "s" ) ) ;
            }

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

        System.out.printf( "%n%nThank you for playing Top This!%n%n" ) ;

        }   // end tearDown()


    /**
     * display introductory message
     *
     * @since 2.0
     */
    private static void welcome()
        {

        System.out.printf( """

                           Welcome to Top This!

                           In this game, players will collect melds and the player with the
                           most melds at the end of the game wins.

                           Respond to any prompt with a period to end the game.

                           Enjoy!
                           """ ) ;

        }   // end welcome()


    /*
     * utility methods
     */


    /**
     * displays a formatted prompt
     *
     * @param prompt
     *     the prompt with optional formatting specifiers
     * @param arguments
     *     argument(s) used by the formatting specifiers
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
     *     the prompt with optional formatting specifiers
     * @param arguments
     *     argument(s) used by the formatting specifiers
     *
     * @return a card as specified by the user or null if no more input is
     *     available or the user requested to exit
     */
    private Card promptForCard( final String prompt,
                                final Object... arguments )
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
     *     the prompt with optional formatting specifiers
     * @param arguments
     *     argument(s) used by the formatting specifiers
     *
     * @return the integer value as specified by the user or -1 if no more input
     *     is available
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

                if ( inputValue > 0 )   // have an int, make sure it's positive
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
     *     the prompt with optional formatting specifiers
     * @param arguments
     *     argument(s) used by the formatting specifiers
     *
     * @return the non-empty line of text as specified by the user with leading
     *     and trailing whitespace removed or null if no more input available
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

    }   // end class TopThis