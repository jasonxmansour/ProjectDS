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


package edu.wit.scds.ds.lists.app.card_game.standard_cards.card ;

import java.util.Arrays ;

// do not modify this code

/**
 * An enumeration of colors of cards in a standard playing card deck.
 *
 * @author David M Rosenberg
 *
 * @version 1.0 - initial version (based on Suit.java)
 * @version 1.1 - corrected spelling of colorDisplayName
 * @version 1.2 2025-03-25 add {@code NONE} for Jokers, etc.
 * @version 2.0 2025-06-26 rename from {@code Color} to {@code Color} to track
 *     other class changes
 * @version 3.0 2025-11-02 rename from {@code Color} to {@code CardColor}
 */
public enum CardColor
    {

    // @formatter:off
    /** for Hearts and Diamonds */
    RED ( "Red" )

    ,/** for Spades and Clubs */
    BLACK ( "Black" )

    ,/** for Jokers, etc. */
    NONE ( "n/a" )

    ;
    // @formatter:on

    /*
     * data fields
     */


    /** 'pretty' (capitalized) form of the color's name */
    private final String displayName ;


    /*
     * constructors
     */


    /**
     * constructor
     *
     * @param colorDisplayName
     *     the 'pretty' (capitalized) form of the color's name
     */
    private CardColor( final String colorDisplayName )
        {

        this.displayName = colorDisplayName ;

        } // end constructor


    /*
     * public methods
     */


    /**
     * Returns the 'pretty' (capitalized) form of the color's name.
     *
     * @return the 'pretty' name of the color
     */
    public String getDisplayName()
        {

        return this.displayName ;

        } // end getDisplayName()


    @Override
    public String toString()
        {

        return getDisplayName() ;

        }	// end toString()


    /*
     * testing/debugging
     */


    /**
     * Test driver
     *
     * @param args
     *     -unused-
     */
    public static void main( final String[] args )
        {

        // display column headers
        System.out.printf( "  %-5s %-10s %-10s %-10s%n", "#", "Name", "Color", "Display Name" ) ;

        // display each element of the enumeration
        Arrays.asList( CardColor.values() ).forEach( aColor ->
            {
            System.out.printf( "  %-5d %-10s %-10s %-10s%n",
                               aColor.ordinal(),
                               aColor.name(),
                               aColor,
                               aColor.displayName ) ;
            } ) ;

        }	// end main()

    } // end enum CardColor