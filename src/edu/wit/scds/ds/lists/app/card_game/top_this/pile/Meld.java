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


package edu.wit.scds.ds.lists.app.card_game.top_this.pile ;

import edu.wit.scds.ds.lists.app.card_game.standard_cards.pile.Pile ;
import edu.wit.scds.ds.lists.app.card_game.universal_base.support.Orientation ;

import static edu.wit.scds.ds.lists.app.card_game.universal_base.support.Orientation.FACE_UP ;

/**
 * represents a set of matching cards, typically three or more, that earn a
 * player points and/or allow them to deplete their hand
 *
 * @author Dave Rosenberg
 *
 * @version 1.0 2025-06-28 Initial implementation (taken from {@code Field}
 */
public final class Meld extends Pile
    {

    /*
     * utility constants
     */
    /** by default, cards added to a meld will be turned face up */
    private final static Orientation DEFAULT_CARD_ORIENTATION = FACE_UP ;


    /*
     * constructors
     */


    /**
     * initialize a meld
     */
    public Meld()
        {

        super( DEFAULT_CARD_ORIENTATION ) ;

        }	// end no-arg constructor


    /**
     * add the cards won to this meld
     *
     * @param cardsToadd
     *     a collection of cards
     */
    public Meld( final Pile cardsToadd )
        {

        this() ;

        super.moveCardsToBottom( cardsToadd ) ;

        }   // end 1-arg constructor


    /*
     * testing/debugging
     */


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

    }	// end class Meld