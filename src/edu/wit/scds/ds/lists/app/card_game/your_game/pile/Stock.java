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


package edu.wit.scds.ds.lists.app.card_game.your_game.pile ;

import edu.wit.scds.ds.lists.app.card_game.standard_cards.card.Card ;
import edu.wit.scds.ds.lists.app.card_game.standard_cards.card.Suit ;
import edu.wit.scds.ds.lists.app.card_game.standard_cards.pile.Deck ;
import edu.wit.scds.ds.lists.app.card_game.standard_cards.pile.Pile ;
import edu.wit.scds.ds.lists.app.card_game.universal_base.support.NoCardsException ;

import static edu.wit.scds.ds.lists.app.card_game.standard_cards.card.Card.newCardLike ;
import static edu.wit.scds.ds.lists.app.card_game.standard_cards.card.Rank.JOKER ;
import static edu.wit.scds.ds.lists.app.card_game.universal_base.support.Orientation.FACE_DOWN ;

// you probably will not modify this code - talk to me first

/**
 * Representation of a stock of cards
 * <p>
 * NOTE: You probably won't modify this code
 * <p>
 * this is the source of all cards available to the game during game play
 *
 * @author Dave Rosenberg
 *
 * @version 1.0 2025-03-31 Initial implementation based on {@code Deck}
 * @version 2.0 2025-06-28
 *     <ul>
 *     <li>track changes to other classes
 *     <li>add constructor that provides source cards
 *     </ul>
 *
 * @author Jason Mansour and Michael Foley
 *
 * @version 3.0 2025-11-03 Initial implementation
 */
public final class Stock extends Pile
    {
    /*
     * utility constants
     */
    // none


    /*
     * data fields
     */
    // none


    /*
     * constructors
     */


    /**
     * Initialize a stock of {@code Cards} with the cards face-down
     */
    public Stock()
        {

        // initialize the pile
        super( FACE_DOWN ) ;

        }	// end no-arg constructor


    /**
     * initialize a stock and populate it with supplied cards
     *
     * @param sourcePile
     *     a pile of cards which will be moved to this stock
     *
     * @since 2.0
     */
    public Stock( final Pile sourcePile )
        {

        super() ;

        moveCardsToBottom( sourcePile ) ;

        }  // end 1-arg constructor


    /*
     * public methods
     */


    /**
     * Pick the top card from the deck
     *
     * @return the top card
     *
     * @throws NoCardsException
     *     if the pile is empty
     */
    public Card drawTopCard() throws NoCardsException
        {

        return removeTopCard() ;

        }  // end drawTopCard()


    /**
     * turn the top card face-up
     */
    public void revealTopCard()
        {

        if ( super.cards.isEmpty() )
            {
            throw new NoCardsException() ;
            }

        getTopCard().reveal() ;

        }  // end revealTopCard()


    /*
     * private utility methods
     */
    // none


    /*
     * for testing/debugging
     */


    /**
     * test driver
     *
     * @param args
     *     -unused-
     */
    public static void main( final String[] args )
        {

        // test rejection of temporary cards
        final Stock myStock = new Stock() ;     // should only accept permanent
                                                // cards
        final Deck cardSource = new Deck() ;    // contains only permanent cards

        // move all the cards from the deck into the stock
        myStock.moveCardsToBottom( cardSource ) ;
        cardSource.moveCardsToBottom( myStock.removeAllMatchingCards( newCardLike( JOKER, Suit.NA ) ) ) ;

        // let's see what's in each pile
        System.out.printf( "deck: %s%n", cardSource.revealAll().toString() ) ;
        System.out.printf( "stock: %s%n", myStock.revealAll().toString() ) ;

        try
            {
            System.out.printf( "add null to myStock:%n" ) ;
            myStock.addToTop( null ) ;
            }
        catch ( final Exception e )
            {
            System.out.printf( "\t%s%n", e ) ;
            }

        try
            {
            System.out.printf( "add a temporary card to myStock:%n" ) ;
            myStock.addToTop( newCardLike( JOKER, Suit.NA ) ) ;
            }
        catch ( final Exception e )
            {
            System.out.printf( "\t%s%n", e ) ;
            }

        }	// end main()

    }	// end class Stock