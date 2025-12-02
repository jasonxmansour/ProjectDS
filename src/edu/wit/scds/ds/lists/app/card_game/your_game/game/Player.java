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
import edu.wit.scds.ds.lists.app.card_game.standard_cards.card.Card.CompareOn ;
import edu.wit.scds.ds.lists.app.card_game.standard_cards.card.Rank ;
import edu.wit.scds.ds.lists.app.card_game.standard_cards.card.Suit ;
import edu.wit.scds.ds.lists.app.card_game.standard_cards.pile.Deck ;
import edu.wit.scds.ds.lists.app.card_game.standard_cards.pile.Pile ;
import edu.wit.scds.ds.lists.app.card_game.universal_base.card.CardBase;
import edu.wit.scds.ds.lists.app.card_game.universal_base.support.NoCardsException ;
import edu.wit.scds.ds.lists.app.card_game.your_game.pile.Hand ;
import edu.wit.scds.ds.lists.app.card_game.your_game.pile.Meld ;
import edu.wit.scds.ds.lists.app.card_game.your_game.pile.Stock ;

import static edu.wit.scds.ds.lists.app.card_game.standard_cards.card.Card.newCardLike ;
import static edu.wit.scds.ds.lists.app.card_game.standard_cards.card.Rank.JOKER ;

import java.io.File ;
import java.io.FileNotFoundException ;
import java.util.ArrayList ;
import java.util.List ;
import java.util.Random ;
import java.util.Scanner ;

// you might modify this code - talk to me first

/**
 * Representation of a player
 * <p>
 * NOTE: You will modify this code
 *
 * @author Dave Rosenberg
 *
 * @version 1.0 2021-12-08 Initial implementation
 * @version 2.0 2025-06-28 track changes to other classes
 * @version 2.1 2025-11-04 track changes to other classes
 *
 * @author Jason Mansour and Michael Foley
 *
 * @version 3.0 2025-11-03 modifications for your game
 */
public final class Player
    {

    /*
     * data fields
     */


    /** player's name */
    public final String name ;

    /** the cards that are in-play */
    private final Hand hand ;

    /** groups of cards collected during play */
    private final List<Meld> melds ;
    
    /** Rummy score tracking */
    private int score ;


    /*
     * constructor(s)
     */


    /**
     * initialize a player
     *
     * @param playerName
     * the player's name
     */
    public Player( final String playerName )
        {

        this.name = playerName ;

        this.hand = new Hand() ;

        this.melds = new ArrayList<>() ;
        
        this.score = 0 ;

        }   // end constructor


    /*
     * public methods
     */


    /**
     * Add a dealt card to our hand
     *
     * @param dealt
     * the card we're dealt
     */
    public void dealtACard( final Card dealt )
        {

        this.hand.addToBottom( dealt ) ;
        this.hand.sort() ;

        }  // end dealtACard()


    /**
     * retrieve the number of melds
     *
     * @return the number of melds
     *
     * @since 2.0
     */
    public int getMeldCount()
        {

        return this.melds.size() ;

        }   // end getMeldCount()


    /**
     * Remove an unspecified card from our hand
     *
     * @return any card currently in the hand
     *
     * @throws NoCardsException
     * if the hand is empty
     */
    public Card playACard() throws NoCardsException
        {

        return this.hand.removeCardAt( new Random().nextInt( 0, this.hand.cardCount() ) ) ;

        }  // end playACard()


    /**
     * Remove a specified card from our hand
     *
     * @param cardToThrow
     * the card to remove
     *
     * @return the specified card or null if not in the hand
     *
     * @since 2.0
     */
    public Card playACard( final Card cardToThrow )
        {

        return this.hand.removeCard( cardToThrow ) ;

        }  // end playACard()


    /**
     * Remove a specified card from our hand
     *
     * @param rank
     * the rank of the card to remove
     * @param suit
     * the suit of the card to remove
     *
     * @return the specified card or null if not in the hand
     */
    public Card playACard( final Rank rank,
                           final Suit suit )
        {

        return playACard( newCardLike( rank, suit ) ) ;

        }  // end playACard()
    
   

    /**
     * Get the card at a specific index without removing it.
     * Used for validating melds before playing.
     *
     * @param index
     * the index of the card to peek at
     * @return the card at the index
     */
    public Card getCardAt( int index ) 
        {
        return (Card) this.hand.getCardAt( index );
        }

    /**
     * Play a card at a specific index (used for Discarding or Melding).
     *
     * @param index
     * the index of the card to remove
     * @return the card removed from the hand
     */
    public Card playCardAt( int index ) 
        {
        return this.hand.removeCardAt( index );
        }

    /**
     * Check if the player has used all their cards.
     *
     * @return true if hand is empty
     */
    public boolean isHandEmpty() 
        {
        return this.hand.isEmpty();
        }

    /**
     * Add a valid meld to the player's collection.
     *
     * @param meld
     * the valid meld to add
     */
    public void addMeld( Meld meld ) 
        {
        this.melds.add( meld );
        }

    /**
     * Calculate points remaining in hand (Deadwood).
     * Face cards = 10, Ace = 1, others = rank value.
     *
     * @return sum of points in hand
     */
    public int calculateHandPoints() 
        {
        int points = 0;
        for ( CardBase c : this.hand ) 
            {
            Card card = (Card) c;
            points += card.rank.getPoints();
            }
        return points;
        }

    /**
     * Add points to the player's total score.
     *
     * @param points
     * points to add
     */
    public void addScore( int points ) 
        {
        this.score += points;
        }

    /**
     * Get current total score.
     *
     * @return total score
     */
    public int getScore() 
        {
        return this.score;
        }
    
    /**
     * Accessor for the hand, primarily used for displaying with indices.
     * * @return the player's hand
     */
    public Hand getHand() 
        {
        return this.hand;
        }
    
    /**
     * Accessor for the player's melds.
     * Used by the game class to display all melds on the table and allow laying off cards.
     * * @return the list of melds owned by this player
     */
    public List<Meld> getMelds() 
        {
        return this.melds;
        }


    /**
     * text describing the contents of the player's hand
     * <p>
     * note that cards' orientation is unchanged
     *
     * @return a string containing the cards in the player's hand
     */
    public String revealHand()
        {

        if ( this.hand.cardCount() == 0 )
            {
            return "empty" ;
            }

        return this.hand.revealAll().toString() ;

        }   // end revealHand()


    /**
     * text describing the contents of the player's melds
     * <p>
     * note that cards' orientation is unchanged
     *
     * @return a string containing the cards in the player's melds
     *
     * @since 2.0
     */
    public String revealMelds()
        {

        if ( this.melds.size() == 0 )
            {
            return "none" ;
            }

        final ArrayList<String> meldsText = new ArrayList<>( this.melds.size() ) ;

        for ( final Meld aMeld : this.melds )
            {
            meldsText.add( aMeld.revealAll().toString() ) ;
            }

        return meldsText.toString() ;

        }   // end revealMelds()


    /**
     * Remove all cards from our hand and our collected cards
     *
     * @return a pile with all the cards we have - order and orientation may be
     * inconsistent
     *
     * @since 2.0
     */
    public Pile turnInAllCards()
        {

        // local temporary class (pile) to hold our cards
        final Pile allCards = new Pile() {} ;

        // we may be holding cards - collect them

        allCards.moveCardsToBottom( this.hand ) ;

        for ( final Pile aMeld : this.melds )
            {
            allCards.moveCardsToBottom( aMeld ) ;
            }

        this.melds.clear() ;

        // assertion: we have no cards, any we had will be returned via allCards

        return allCards ;

        }  // end turnInAllCards()


    /**
     * record that we won a round
     *
     * @param cardsWon
     * the cards this player won
     *
     * @since 2.0
     */
    public void wonRound( final Pile cardsWon )
        {

        // save the cards as a meld
        final Meld newMeld = new Meld( cardsWon ) ;
        newMeld.revealAll() ;

        this.melds.add( newMeld ) ;

        }   // end cardsWon()


    /*
     * utility methods
     */


    @Override
    public String toString()
        {

        return String.format( "%nPlayer: %s%n\thand: %s%n\tmelds: %s",
                              this.name,
                              revealHand(),
                              revealMelds().replace( ", [", "[" )
                                           .replace( "[[", "[" )
                                           .replace( "]]", "]" )
                                           .replace( "[", "\n\t\t[" ) ) ;

        }   // end toString()


    /*
     * testing/debugging
     */


    /**
     * (optional) test driver
     *
     * @param args
     * -unused-
     */
    public static void main( final String[] args )
        {

        // we'll sort by rank only and treat ace as highest value card
        Card.setCompareOnAttributes( CompareOn.COMPARE_RANK_ONLY ) ;
        Rank.setUseAltOrder( true ) ;

        final Deck testDeck = new Deck() ;

        // create the stock initially populated with all the cards from the deck
        final Stock testStock = new Stock( testDeck ) ;

        // put any jokers back in the deck

        // CHALLENGE what's the efficiency of moving all the jokers?
        // CHALLENGE can it be O(1)?  if so, how?

        final Card lookupJoker = new Card( JOKER ) ;
        Card foundJoker ;

        while ( ( foundJoker = testStock.removeCard( lookupJoker ) ) != null )
            {
            testDeck.addToBottom( foundJoker ) ;
            }

        // shuffle them
        testStock.shuffle() ;

        testStock.revealAll() ;
        System.out.printf( "Stock: %s%n%n", testStock ) ;
        testStock.hideAll() ;

        testDeck.revealAll() ;
        System.out.printf( "Deck: %s%n%n", testDeck ) ;
        testDeck.hideAll() ;


        final Player testPlayer = new Player( "tester" ) ;

        System.out.printf( "start: %s%n", testPlayer ) ;

        for ( int i = 1 ; i <= 5 ; i++ )
            {
            final Card dealt = testStock.drawTopCard().reveal() ;

            testPlayer.dealtACard( dealt ) ;
            }

        System.out.printf( "%ndealt: %s%n", testPlayer ) ;

        for ( int i = 1 ; i <= 3 ; i++ )
            {
            final Pile someCards = new Pile() {}.setDefaultFaceUp() ;

            for ( int j = 1 ; j <= 5 ; j++ )
                {
                someCards.addToTop( testStock.drawTopCard() ) ;
                }

            testPlayer.wonRound( someCards ) ;
            }

        System.out.printf( "%nwith some melds: %s%n", testPlayer ) ;


        // the following is the correct way to access a file in the data folder
        System.out.printf( "%n%naccessing a file in the data folder:%n%n" ) ;

        try ( Scanner input = new Scanner( new File( "./data/readme.txt" ) ) ; )
            {

            while ( input.hasNextLine() )
                {
                System.out.printf( "%s%n", input.nextLine() ) ;
                }

            }
        catch ( final FileNotFoundException e )
            {
            System.err.printf( "failed to open readme.txt:%n%s%n", e ) ;
            }

        }   // end main()

    }   // end class Player