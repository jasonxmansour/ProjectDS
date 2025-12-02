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
import edu.wit.scds.ds.lists.app.card_game.standard_cards.pile.Pile ;
import edu.wit.scds.ds.lists.app.card_game.universal_base.support.NoCardsException ;

import static edu.wit.scds.ds.lists.app.card_game.universal_base.support.Orientation.FACE_UP ;

// you might modify this code - talk to me if you have any questions

/**
 * Representation of a discard pile
 * <p>
 * NOTE: You might modify this code
 *
 * @author Dave Rosenberg
 *
 * @version 1.0 2021-12-08 Initial implementation
 * @version 2.0 2025-03-30 track changes to all classes
 * @version 3.0 2025-06-26 track changes to all classes
 *
 * @author Your Name
 *
 * @version 4.0 2025-11-03 modifications for your implementation
 */
public final class DiscardPile extends Pile
    {


    /*
     * constructors
     */


    /**
     * initialize a discard pile with cards placed face down by default
     */
    public DiscardPile()
        {

        super( FACE_UP ) ;

        }	// end no-arg constructor


    /*
     * public methods
     */


    /**
     * place {@code discard} on the top of discard pile
     *
     * @param discard
     *     the card to place on the top of the discard pile
     */
    public void addCard( final Card discard )
        {

        super.addToTop( discard ) ;

        }   // end addCard()


    @Override
    public Card getTopCard()
        {

        return (Card) super.getTopCard() ;

        }   // end getTopCard()


    /**
     * get the card from the top of the discard pile but does not remove it
     *
     * @return the top card from the discard pile turned face up
     *
     * @throws NoCardsException
     *     if the pile is empty
     */
    public Card lookAtTopCard() throws NoCardsException
        {

        return getTopCard().reveal() ;

        }   // end lookAtTopCard()


    /**
     * take the card from the top of the discard pile
     *
     * @return the top card from the discard pile turned face up
     *
     * @throws NoCardsException
     *     if the pile is empty
     */
    public Card takeTopCard() throws NoCardsException
        {

        return super.removeTopCard() ;

        }   // end takeTopCard()


    /**
     * (optional) test driver
     *
     * @param args
     *     -unused-
     */
    public static void main( final String[] args )
        {
        // TODO Auto-generated method stub

        }	// end main()

    }	// end class DiscardPile