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


package edu.wit.scds.ds.lists.app.card_game.universal_base.support ;

// do not modify this code

/**
 * Represents the orientation of a {@code Card} (face up or face down). When a {@code Card} is face
 * up, its designation (for example, "A♦") is visible and will be displayed. When face down, a
 * placeholder designation is displayed (for example, "--").
 *
 * @author David M Rosenberg
 *
 * @version 1.0 2025-06-26 Initial implementation - replaces boolean constants for face up/down
 *     specification
 * @version 2.0 2025-07-12 add {@code AS_IS} and {@code FLIP} to support pile-level specification
 */
public enum Orientation
    {

    // @formatter:off
    // these are applicable to cards and piles

    /** designation is visible */
    FACE_UP ( "Face up", "visible" )

    , /** designation is not visible */
    FACE_DOWN ( "Face down", "not visible" )


    // these are only applicable to piles when cards are being added

    , /** don't change visibility */
    AS_IS ( "As is", "don't change visibility" )

    , /** flip cards when added to a pile */
    FLIP ( "Flip", "reverse visibility" )

    ;
    // @formatter:on
    

    /*
     * static utilities
     */


    /** control {@code toString()}'s behavior wrt which field it will return */
    private enum Selector
        {
        
        // @formatter:off
        /** {@code toString()} will return the display name */
        USE_DISPLAY_NAME

        , /** {@code toString()} will return the description */
        USE_DESCRIPTION

        ;
        // @formatter:on
        
        }   // end enum Selector

    /** switch between the display name and description for {@code toString()} */
    private static Selector toStringSwitch = Selector.USE_DISPLAY_NAME ;


    /*
     * data fields
     */


    /** 'pretty' (capitalized) form of the orientation name */
    private final String displayName ;

    /** functional description of a Card's face's visibility */
    private final String description ;


    /*
     * constructors
     */


    /**
     * constructor
     *
     * @param theDisplayName
     *     'pretty' (capitalized) form of the orientation's name
     * @param theDescription
     *     description of the orientation's visibility
     */
    private Orientation( final String theDisplayName,
                         final String theDescription )
        {

        this.displayName = theDisplayName ;
        this.description = theDescription ;

        }   // end constructor


    /*
     * public methods
     */


    /**
     * reverse the orientation (face up/down) of the card
     *
     * @return the opposite orientation
     */
    public Orientation flip()
        {

        if ( ( this != FACE_UP ) && ( this != FACE_DOWN ) )
            {
            throw new IllegalStateException( String.format( "can't flip %s",
                                                            name() ) ) ;
            }

        return this == FACE_UP
            ? FACE_DOWN
            : FACE_UP ;

        }   // end flip()


    /**
     * Returns the 'pretty' (capitalized) form of the orientation's name.
     *
     * @return the 'pretty' name
     */
    public String getDisplayName()
        {

        return this.displayName ;

        }   // end getDisplayName()


    /**
     * Returns the orientation's visibility description.
     *
     * @return the 'pretty' description
     */
    public String getVisibilityDescription()
        {

        return this.description ;

        }   // end getVisibilityDescription()


    /**
     * select display name as return value from {@code toString()}
     *
     * @return this orientation (fluent)
     */
    public Orientation selectName()
        {

        toStringSwitch = Selector.USE_DISPLAY_NAME ;

        return this ;

        }   // end selectName()


    /**
     * select description as return value from {@code toString()}
     *
     * @return this orientation (fluent)
     */
    public Orientation selectDescription()
        {

        toStringSwitch = Selector.USE_DESCRIPTION ;

        return this ;

        }   // end selectVisibility()


    @Override
    public String toString()
        {

        return toStringSwitch == Selector.USE_DISPLAY_NAME
            ? this.displayName
            : this.description ;

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
        System.out.printf( "  %-5s %-10s %-15s %-15s %-15s%n",
                           "#",
                           "Name",
                           "Orientation",
                           "Display Name",
                           "Visibility" ) ;

        // display each element of the enumeration
        for ( final Orientation anOrientation : Orientation.values() )
            {
            System.out.printf( "  %-5d %-10s %-15s %-15s %-15s%n",
                               anOrientation.ordinal(),
                               anOrientation.name(),
                               anOrientation,
                               anOrientation.displayName,
                               anOrientation.description ) ;
            }	// end for

        System.out.printf( "%nselect name or description%n" ) ;

        final Orientation faceUp = FACE_UP ;

        System.out.printf( "name: %s%n", faceUp.selectName().toString() ) ;
        System.out.printf( "description: %s%n",
                           faceUp.selectDescription().toString() ) ;

        }	// end main()

    }   // end enum Orientation