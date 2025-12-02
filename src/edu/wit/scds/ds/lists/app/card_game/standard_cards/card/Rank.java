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
 * An enumeration of card ranks.
 *
 * @author David M Rosenberg
 *
 * @version 1.0 2016-03-16 initial version
 * @version 1.1 2022-11-06 add switches for standard vs alternate points and
 *     order
 * @version 2.0 2025-03-29
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
 *     <li>rename from {@code Rank} to {@code Rank} to track other class changes
 *     <li>swap (alt) point and (alt) order values
 *     </ul>
 */
public enum Rank
    {

// @formatter:off
//  Element     Display Name  Abbreviation  Graphic     Points   Alt Points      Order    Alt Order
    /** Ace */
    ACE     (   "Ace",          "A",          "A",         1,        11,           1,        14 )
    , /** Two */
    TWO     (   "Duece",        "2",          "2",         2,         2,           2,         2 )
    , /** Three */
    THREE   (   "Three",        "3",          "3",         3,         3,           3,         3 )
    , /** Four */
    FOUR    (   "Four",         "4",          "4",         4,         4,           4,         4 )
    , /** Five */
    FIVE    (   "Five",         "5",          "5",         5,         5,           5,         5 )
    , /** Six */
    SIX     (   "Six",          "6",          "6",         6,         6,           6,         6 )
    , /** Seven */
    SEVEN   (   "Seven",        "7",          "7",         7,         7,           7,         7 )
    , /** Eight */
    EIGHT   (   "Eight",        "8",          "8",         8,         8,           8,         8 )
    , /** Nine */
    NINE    (   "Nine",         "9",          "9",         9,         9,           9,         9 )
    , /** Ten */
    TEN     (   "Ten",          "T",         "10",        10,        10,          10,        10 )
    , /** Jack */
    JACK    (   "Jack",         "J",          "J",        10,        10,          11,        11 )
    , /** Queen */
    QUEEN   (   "Queen",        "Q",          "Q",        10,        10,          12,        12 )
    , /** King */
    KING    (   "King",         "K",          "K",        10,        10,          13,        13 )
    , /** Joker */
    JOKER   (   "Joker",        "R",          "R",         0,         0,          99,         0 )
    ;
// @formatter:on


    /*
     * constants
     */


    /** indicate rank should use alt priority rather than regular priority */
    public final static boolean USE_REGULAR_POINTS = false ;
    /** indicate rank should use regular priority rather than alt priority */
    public final static boolean USE_ALT_POINTS = true ;

    /** indicate rank should use alt priority rather than regular priority */
    public final static boolean USE_REGULAR_ORDER = false ;
    /** indicate rank should use regular priority rather than alt priority */
    public final static boolean USE_ALT_ORDER = true ;


    /*
     * static fields
     */


    /**
     * when true, evaluations will use {@code altPoints} instead of
     * {@code points}
     */
    private static boolean useAltPoints = USE_REGULAR_POINTS ;
    /**
     * when true, evaluations will use {@code altOrder} instead of {@code order}
     */
    private static boolean useAltOrder = USE_REGULAR_ORDER ;


    /*
     * data fields
     */


    /** 'pretty' name for the rank */
    private final String displayName ;

    /** abbreviated name for the rank */
    private final String abbreviatedName ;

    /** graphic representation of the rank */
    private final String graphic ;

    /** points for a card of this rank */
    private final int points ;

    /** alternate points for a card of this rank */
    private final int altPoints ;

    /** sort order */
    private final int order ;

    /** alternate sort order */
    private final int altOrder ;


    /*
     * constructor
     */


    /**
     * Sets all fields representing a card rank
     *
     * @param rankDisplayName
     *     more esthetically pleasing for display
     * @param rankAbbreviatedName
     *     abbreviation for the rank, typically used for user suit selection
     * @param rankGraphic
     *     the 'standard' icon
     * @param rankPoints
     *     numeric value for the card
     * @param rankAltPoints
     *     alternate value for the card (e.g., Ace can be worth 1 or 11 points)
     * @param rankOrder
     *     numeric order for the card
     * @param rankAltOrder
     *     alternate order for the card (e.g., Ace can have the lowest or
     *     highest order)
     */
    private Rank( final String rankDisplayName,
                  final String rankAbbreviatedName,
                  final String rankGraphic,
                  final int rankPoints,
                  final int rankAltPoints,
                  final int rankOrder,
                  final int rankAltOrder )
        {

        this.displayName = rankDisplayName ;
        this.abbreviatedName = rankAbbreviatedName ;
        this.graphic = rankGraphic ;
        this.points = rankPoints ;
        this.altPoints = rankAltPoints ;
        this.order = rankOrder ;
        this.altOrder = rankAltOrder ;

        } // end constructor


    /*
     * public methods
     */


    /**
     * Retrieves the abbreviated name
     *
     * @return the abbreviated name
     *
     * @since 2.0
     */
    public String getAbbreviatedName()
        {

        return this.abbreviatedName ;

        }   // end getAbbreviatedName()


    /**
     * Retrieves the alternate order flag
     *
     * @return the alternate order
     */
    public int getAltOrder()
        {

        return this.altOrder ;

        } // end getAltOrder()


    /**
     * Retrieves the alternate point value
     *
     * @return the alternate point value
     */
    public int getAltPoints()
        {

        return this.altPoints ;

        } // end getAltPoints()


    /**
     * Retrieves the display name
     *
     * @return the display name
     */
    public String getDisplayName()
        {

        return this.displayName ;

        } // end getDisplayName()


    /**
     * Retrieves the graphic/icon
     *
     * @return the graphic/icon
     */
    public String getGraphic()
        {

        return this.graphic ;

        } // end getGraphic()


    /**
     * Retrieves the order flag (regular or alternate)
     *
     * @return the order
     */
    public int getOrder()
        {

        return Rank.useAltOrder == USE_ALT_ORDER
                ? this.altOrder
                : this.order ;

        } // end getOrder()


    /**
     * Retrieves the point value
     *
     * @return the point value
     */
    public int getPoints()
        {

        return Rank.useAltPoints == USE_ALT_POINTS
                ? this.altPoints
                : this.points ;

        } // end getPoints()


    /**
     * Retrieves the current setting of {@code useAltOrder}
     *
     * @return the current setting of {@code useAltOrder}
     */
    public static boolean getUseAltOrder()
        {

        return Rank.useAltOrder ;

        }   // getUseAltOrder()


    /**
     * Retrieves the current setting of {@code useAltPoints}
     *
     * @return the current setting of {@code useAltPoints}
     */
    public static boolean getUseAltPoints()
        {

        return Rank.useAltPoints ;

        }   // getUseAltPoints()


    /**
     * Sets {@code useAltOrder}
     *
     * @param newUseAltOrder
     *     the new setting
     *
     * @return the previous setting of {@code useAltOrder}
     */
    public static boolean setUseAltOrder( final boolean newUseAltOrder )
        {

        final boolean wasUseAltOrder = Rank.useAltOrder ;

        Rank.useAltOrder = newUseAltOrder ;

        return wasUseAltOrder ;

        }   // setUseAltOrder()


    /**
     * Sets {@code useAltPoints}
     *
     * @param newUseAltPoints
     *     the new setting
     *
     * @return the previous setting of {@code useAltPoints}
     */
    public static boolean setUseAltPoints( final boolean newUseAltPoints )
        {

        final boolean wasUseAltPoints = Rank.useAltPoints ;

        Rank.useAltPoints = newUseAltPoints ;

        return wasUseAltPoints ;

        }   // setUseAltPoints()


    /*
     * utility methods
     */


    /**
     * display the abbreviation for each rank
     *
     * @since 2.0
     */
    public static void displayHelp()
        {

        System.out.printf( "%n%nTo specify a rank, use the abbreviation:%n%n" ) ;

        Arrays.asList( Rank.values() ).forEach( aRank ->
            {
            System.out.printf( "%2s -> \"%s\" \"%s\"%n",
                               aRank.abbreviatedName,
                               aRank.graphic,
                               aRank.displayName ) ;
            } ) ;

        System.out.printf( "%n" ) ;

        }   // end displayHelp()


    /**
     * Parse a text description of rank
     *
     * @param rankDescription
     *     a name to parse
     *
     * @return the corresponding enum constant or null if the name is
     *     unrecognized
     */
    public static Rank interpretDescription( final String rankDescription )
        {

        if ( ( rankDescription == null ) || ( rankDescription.length() < 1 ) )
            {
            return null ;   // nothing to parse
            }

        if ( "?".equals( rankDescription ) )
            {
            displayHelp() ;

            return null ;
            }

        final String forComparison = rankDescription.toUpperCase() ;

        for ( final Rank aRank : Rank.values() )
            {

            if ( aRank.abbreviatedName.equals( forComparison ) )
                {
                // found a match
                return aRank ;
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

        }	// end toString()


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
        System.out.printf( " %-4s %-8s %-8s %-12s   %-12s %-15s %-6s   %-10s   %-10s %-15s%n",
                           "#",
                           "Rank",
                           "Graphic",
                           "Abbreviation",
                           "Name",
                           "Display Name",
                           "Points",
                           "Alt Points",
                           "Order",
                           "Alt Order" ) ;


        // display each element of the enumeration
        Arrays.asList( Rank.values() ).forEach( aRank ->
            {
            System.out.printf( "%2d     %2s         %2s          %-6s   %-12s %-15s   %2d        %2d         %2d           %2d%n",
                               aRank.ordinal(),
                               aRank,
                               aRank.graphic,
                               aRank.abbreviatedName,
                               aRank.name(),
                               aRank.displayName,
                               aRank.points,
                               aRank.altPoints,
                               aRank.order,
                               aRank.altOrder ) ;
            } ) ;	// end for

        // display help
        displayHelp() ;

        }   // end main()

    } // end enum Rank