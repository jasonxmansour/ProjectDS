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

import static edu.wit.scds.ds.lists.app.card_game.standard_cards.card.CardColor.BLACK ;
import static edu.wit.scds.ds.lists.app.card_game.standard_cards.card.CardColor.NONE ;
import static edu.wit.scds.ds.lists.app.card_game.standard_cards.card.CardColor.RED ;

import java.util.Arrays ;

// do not modify this code

/**
 * An enumeration of card suits. (based upon Listing C-2 of Appendix C.)
 * <p>
 * You may want/need to adjust the priorities for your game. As provided, suits
 * are switch-selectably ordered by priority:
 * <pre>
 * Spades (highest) -> Diamonds -> Hearts -> Clubs -> none (lowest)
 * </pre>
 * or alternate priority:
 * <pre>
 * none (highest) -> Clubs -> Hearts -> Diamonds -> Spades (lowest)
 * </pre>
 *
 * @author Frank M. Carrano
 * @author Timothy M. Henry
 *
 * @version 4.0
 *
 * @author David M Rosenberg
 *
 * @version 4.1 2016-03-16
 *     <ul>
 *     <li>enhanced definition: added display name and graphic
 *     <li>added test driver main()
 *     </ul>
 * @version 4.1.1 2021-11-19
 *     <ul>
 *     <li>fill in Javadoc comments
 *     <li>add Comparator/compare()
 *     </ul>
 * @version 4.2 2022-11-06 add switch for standard vs alternate priority
 * @version 5.0 2025-03-25
 *     <ul>
 *     <li>change color from {@code String} to {@code Color}
 *     <li>change {@code NONE} to {@code NA} to distinguish from
 *     {@code Color.NONE}
 *     </ul>
 * @version 6.0 2025-03-29
 *     <ul>
 *     <li>add abbreviated name to support user selection
 *     <li>revise {@code interpretDescription} to select based on abbreviated
 *     name rather than hardcoded values
 *     <li>add {@code displayHelp()} to support user specification/selection
 *     <li>add various public "USE_" constants to support more mnemonic
 *     programming
 *     </ul>
 * @version 7.0 2025-06-26
 *     <ul>
 *     <li>rename from {@code Suit} to {@code Suit} to track other class changes
 *     <li>swap priority and alt priority values
 *     </ul>
 */
public enum Suit
    {

// @formatter:off
//  Element         Display Name    Abbreviation Graphic     Color      Priority   Alt Priority
    /** Spades suit */
    SPADES      (   "Spades",       "S",         "♠",        BLACK,      1,        4 )
    , /** Diamonds suit */
    DIAMONDS    (   "Diamonds",     "D",         "♦",        RED,        2,        3 )
    , /** Hearts suit */
    HEARTS      (   "Hearts",       "H",         "♥",        RED,        3,        2 )
    , /** Clubs suit */
    CLUBS       (   "Clubs",        "C",         "♣",        BLACK,      4,        1 )
    , /** no suit - for cards such as Joker */
    NA          (   "",             "N",         "",         NONE,       5,        0 )
    ;
// @formatter:on

    /*
     * constants
     */


    /** indicate rank should use alt priority rather than regular priority */
    public final static boolean USE_REGULAR_PRIORITY = false ;
    /** indicate rank should use regular priority rather than alt priority */
    public final static boolean USE_ALT_PRIORITY = true ;


    /*
     * static fields
     */


    /**
     * when true, evaluations will use {@code altPriority} instead of
     * {@code priority}
     */
    private static boolean useAltPriority = USE_REGULAR_PRIORITY ;


    /*
     * data fields
     */


    /** 'pretty' name for the suit */
    private final String displayName ;

    /** abbreviated name for the suit */
    private final String abbreviatedName ;

    /** graphic representation of the suit */
    private final String graphic ;

    /** black or red for a standard, 52-card deck */
    private final CardColor color ;

    /** relative strength of each suite - may be more natural for comparisons */
    private final int priority ;

    /** alternative priority ordering - may be more natural for display */
    private final int altPriority ;


    /*
     * constructor
     */


    /**
     * Sets all fields representing a card suit
     *
     * @param suitDisplayName
     *     more esthetically pleasing for display
     * @param suitAbbreviatedName
     *     abbreviation for the suit, typically used for user suit selection
     * @param suitGraphic
     *     the 'standard' icon for the suit
     * @param suitColor
     *     the 'standard' color for the suit
     * @param suitPriority
     *     relative value/priority of the suit - used by compare()
     * @param altSuitPriority
     *     alternate relative value/priority of the suit - used by compare()
     */
    private Suit( final String suitDisplayName,
                  final String suitAbbreviatedName,
                  final String suitGraphic,
                  final CardColor suitColor,
                  final int suitPriority,
                  final int altSuitPriority )
        {

        this.displayName = suitDisplayName ;
        this.abbreviatedName = suitAbbreviatedName ;
        this.graphic = suitGraphic ;
        this.color = suitColor ;
        this.priority = suitPriority ;
        this.altPriority = altSuitPriority ;

        }   // end constructor


    /*
     * public methods
     */


    /**
     * Retrieves the abbreviated name
     *
     * @return the abbreviated name
     *
     * @since 6.0
     */
    public String getAbbreviatedName()
        {

        return this.abbreviatedName ;

        }   // end getAbbreviatedName()


    /**
     * Retrieves the alternate priority
     *
     * @return the alternate priority
     */
    public int getAltPriority()
        {

        return this.altPriority ;

        }   // end getAltPriority()


    /**
     * Retrieves the color
     *
     * @return the color
     */
    public CardColor getColor()
        {

        return this.color ;

        }   // end getColor()


    /**
     * Retrieves the display name
     *
     * @return the display name
     */
    public String getDisplayName()
        {

        return this.displayName ;

        }   // end getDisplayName()


    /**
     * Retrieves the graphic/icon
     *
     * @return the graphic/icon
     */
    public String getGraphic()
        {

        return this.graphic ;

        }   // end getGraphic()


    /**
     * Retrieves the priority
     *
     * @return the priority or alternate priority depending upon the setting of
     *     {@code useAltPriority}
     */
    public int getPriority()
        {

        return Suit.useAltPriority == USE_ALT_PRIORITY
                ? this.altPriority
                : this.priority ;

        }   // end getPriority()


    /**
     * Retrieves the current setting of {@code useAltPriority}
     *
     * @return the current setting of {@code useAltPriority}
     */
    public static boolean getUseAltPriority()
        {

        return Suit.useAltPriority ;

        }   // getUseAltPriority()


    /**
     * Set {@code useAltPriority}
     *
     * @param newUseAltPriority
     *     the new setting
     *
     * @return the previous setting of {@code useAltPriority}
     */
    public static boolean setUseAltPriority( final boolean newUseAltPriority )
        {

        final boolean wasUseAltPriority = Suit.useAltPriority ;

        Suit.useAltPriority = newUseAltPriority ;

        return wasUseAltPriority ;

        }   // setUseAltPriority()


    /*
     * utility methods
     */


    /**
     * display the abbreviation for each suit
     *
     * @since 6.0
     */
    public static void displayHelp()
        {

        System.out.printf( "%n%nTo specify a suit, use the abbreviation:%n%n" ) ;

        Arrays.asList( Suit.values() ).forEach( aSuit ->
            {
            System.out.printf( "%s -> \"%s\" \"%s\"%n",
                               aSuit.abbreviatedName,
                               aSuit.graphic,
                               aSuit.displayName ) ;
            } ) ;

        System.out.printf( "%n" ) ;

        }   // end displayHelp()


    /**
     * Parse a text description of suit
     *
     * @param suitDescription
     *     a name to parse
     *
     * @return the corresponding enum constant or null if the name is
     *     unrecognized
     */
    public static Suit interpretDescription( final String suitDescription )
        {

        if ( ( suitDescription == null ) || ( suitDescription.length() < 1 ) )
            {
            return null ;   // nothing to parse
            }

        if ( "?".equals( suitDescription ) )
            {
            displayHelp() ;

            return null ;
            }

        final String forComparison = suitDescription.toUpperCase() ;

        for ( final Suit aSuit : Suit.values() )
            {

            if ( aSuit.abbreviatedName.equals( forComparison ) )
                {
                // found a match
                return aSuit ;
                }

            }

        // no match
        return null ;

        }   // end method interpretDescription()


    /**
     * For display, use the graphic/icon<br>
     * {@inheritDoc}
     */
    @Override
    public String toString()
        {

        return this.graphic ;

        }   // end toString()


    /*
     * inner classes
     */
    // none


    /*
     * for testing/debugging
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
        System.out.printf( "%-5s %-8s %-8s %-12s    %-12s %-15s %-10s %-10s %-10s%n",
                           "#",
                           "Suit",
                           "Graphic",
                           "Abbreviation",
                           "Name",
                           "Display Name",
                           "Color",
                           "Priority",
                           "Alt Priority" ) ;

        // display each element of the enumeration
        Arrays.asList( Suit.values() ).forEach( aSuit ->
            {
            System.out.printf( "%-5d   %-8s  %-3s        %-7s    %-12s %-15s %-10s    %2d          %2d%n",
                               aSuit.ordinal(),
                               aSuit,
                               aSuit.graphic,
                               aSuit.abbreviatedName,
                               aSuit.name(),
                               aSuit.displayName,
                               aSuit.color,
                               aSuit.getPriority(),
                               aSuit.getAltPriority() ) ;
            } ) ;

        // display help
        displayHelp() ;

        }	// end main()

    }   // end enum Suit