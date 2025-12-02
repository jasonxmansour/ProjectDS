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


package edu.wit.scds.ds.lists.app.card_game.universal_base.card ;

import edu.wit.scds.ds.lists.app.card_game.universal_base.support.Orientation ;
import edu.wit.scds.ds.lists.app.card_game.universal_base.support.Persistence ;

import static edu.wit.scds.ds.lists.app.card_game.universal_base.support.Orientation.FACE_DOWN ;
import static edu.wit.scds.ds.lists.app.card_game.universal_base.support.Orientation.FACE_UP ;
import static edu.wit.scds.ds.lists.app.card_game.universal_base.support.Persistence.PERMANENT ;
import static edu.wit.scds.ds.lists.app.card_game.universal_base.support.Persistence.TEMPLATE ;
import static edu.wit.scds.ds.lists.app.card_game.universal_base.support.Persistence.TEMPORARY ;

import java.util.Arrays ;
import java.util.Objects ;

// do not modify this code

/**
 * representation of any kind of playing card providing 'universal'
 * functionality
 * <p>
 * NOTE: Do not modify this code
 *
 * @author David M Rosenberg
 *
 * @version 1.0 2025-06-26 Initial implementation
 * @version 2.0 2025-07-11
 *     <ul>
 *     <li>rename from {@code Card} to {@code UniversalBaseCard}
 *     <li>add persistence
 *     <li>make most instance methods fluent
 *     <li>eliminate all constructors except no-arg and 1-arg supplying face up
 *     text
 *     <li>add {@code null}-argument checking
 *     <li>move {@code Persistence} to its own source file
 *     </ul>
 * @version 3.0 2025-11-02 rename from {@code UniversalBaseCard} to
 *     {@code CardBase} - less of a mouthful
 * @version 3.1 2025-11-11 add {get,set}FaceUpTextLocked()
 * @version 3.2 2025-11-19 add template card support
 */
public abstract class CardBase implements Comparable<CardBase>
    {

    /*
     * constants
     */


    /** default text to display when a card is face up */
    protected static final String DEFAULT_FACEUP_TEXT = "??" ;

    /** default text to display when a card is face down */
    protected static final String DEFAULT_FACEDOWN_TEXT = "--" ;

    /** default orientation */
    protected static final Orientation DEFAULT_ORIENTATION = FACE_DOWN ;


    /** default persistence - used for matching and lookup */
    protected static final Persistence DEFAULT_PERSISTENCE = TEMPORARY ;

    /**
     * default lack of decoration for cards where [0] is applied immediately
     * before the face text and [1] immediately following
     */
    protected static final String[] DEFAULT_NO_DECORATION = { "", "" } ;

    /**
     * default decoration for permanent cards where [0] is applied immediately
     * before the face text and [1] immediately following
     */
    protected static final String[] DEFAULT_PERMANENT_DECORATION = { "", "" } ;

    /**
     * default decoration for template cards where [0] is applied immediately
     * before the face text and [1] immediately following
     */
    protected static final String[] DEFAULT_TEMPLATE_DECORATION = { "├", "┤" } ;

    /**
     * default decoration for temporary cards where [0] is applied immediately
     * before the face text and [1] immediately following
     */
    protected static final String[] DEFAULT_TEMPORARY_DECORATION = { "▸", "◂" } ;

    /**
     * temporary decoration initializer
     */
    protected static final String[] DEFAULT_UNKNOWN_DECORATION = { "??", "??" } ;


    /*
     * static fields
     */


    /**
     * the text to display when the card is face up - if not specified, use
     * current default
     */
    protected static String defaultFaceUpText = DEFAULT_FACEUP_TEXT ;

    /**
     * the text to display when the card is face down - if not specified, use
     * current default
     */
    protected static String defaultFaceDownText = DEFAULT_FACEDOWN_TEXT ;


    /** default state of a card when instantiated - face up or down */
    protected static Orientation defaultOrientation = DEFAULT_ORIENTATION ;

    /**
     * default state for decoration for permanent cards where [0] is applied
     * immediately before the face text and [1] immediately following
     */
    protected static String[] defaultPermanentCardDecoration = DEFAULT_PERMANENT_DECORATION ;

    /**
     * default state for decoration for template cards where [0] is applied
     * immediately before the face text and [1] immediately following
     */
    protected static String[] defaultTemplateCardDecoration = DEFAULT_TEMPLATE_DECORATION ;

    /**
     * default state for decoration for temporary cards where [0] is applied
     * immediately before the face text and [1] immediately following
     */
    protected static String[] defaultTemporaryCardDecoration = DEFAULT_TEMPORARY_DECORATION ;


    /** control instantiated cards' persistence */
    protected static Persistence defaultPersistence = DEFAULT_PERSISTENCE ;


    /** enable/disable decoration */
    protected enum EnabledDisabled
        {
        // @formatter:off

         /** include decoration */
         ENABLED

         , /** do not include decoration */
         DISABLED

         // @formatter:on
        }


    /** enable/disable decoration */
    protected static EnabledDisabled includeDecoration = EnabledDisabled.ENABLED ;


    /*
     * data fields
     */


    /** text to display when the card is face up */
    private String faceUpText ;

    /** lock for faceUpText - it's supposed to be immutable */
    private boolean faceUpTextLocked ;

    /** text to display when the card is face down */
    private String faceDownText ;

    /** control display - face up/down */
    protected Orientation orientation ;

    /**
     * permanent, template (used during deck instantiation), temporary (used for
     * lookup/matching)
     */
    private final Persistence persistence ;

    /** decoration applied before/after the face text */
    private final String[] decoration ;

    /*
     * constructors
     */


    /**
     * set initial state to default values
     */
    protected CardBase()
        {

        this( defaultPersistence ) ;

        }   // end no-arg constructor


    /**
     * set initial state to default values with specified persistence
     * <p>
     * Note: this constructor is typically used by a deck to create permanent
     * cards from templates in its constructor or during game play for cloning a
     * card which should be temporary for matching, lookup, etc.
     *
     * @param cardPersistence
     *     the persistence specifically for this card
     *
     * @since 2.0
     */
    protected CardBase( final Persistence cardPersistence )
        {

        validatePersistence( cardPersistence ) ;

        this.faceUpText = defaultFaceUpText ;
        this.faceUpTextLocked = false ;

        this.faceDownText = defaultFaceDownText ;

        this.orientation = defaultOrientation ;

        this.persistence = cardPersistence ;

        this.decoration = new String[ DEFAULT_UNKNOWN_DECORATION.length ] ;
        setDecoration() ;

        }   // end 1-arg constructor w/ persistence


    /**
     * set initial state to specified face up text and default orientation
     *
     * @param initialFaceUpText
     *     text to display when the card is face up
     *
     * @since 2.0
     */
    protected CardBase( final String initialFaceUpText )
        {

        this() ;

        setFaceUpText( initialFaceUpText ) ;

        }   // end 1-arg constructor w/ face up text


    /**
     * create a copy of this card, typically temporary if we're permanent,
     * permanent if we're template
     * <p>
     * only {@code CardBase} fields are copied; the subclass is responsible for
     * copying everything else
     *
     * @param sourceCard
     *     the card to clone
     * @param cardPersistence
     *     the persistence for the clone
     *
     * @since 3.2
     */
    protected CardBase( final CardBase sourceCard,
                        final Persistence cardPersistence )
        {

        Objects.requireNonNull( sourceCard, "sourceCard" ) ;

        validatePersistence( cardPersistence ) ;

        this.faceUpText = sourceCard.faceUpText ;
        this.faceUpTextLocked = true ;

        this.faceDownText = sourceCard.faceDownText ;

        this.orientation = sourceCard.orientation ;

        this.persistence = cardPersistence ;

        this.decoration = new String[ DEFAULT_UNKNOWN_DECORATION.length ] ;
        setDecoration() ;

        }   // end cloning constructor


    /*
     * public API methods
     */


    /**
     * Flip a card over
     *
     * @return this card (fluent)
     */
    public CardBase flip()
        {

        this.orientation = this.orientation.flip() ;

        return this ;

        }  // end flip()


    /**
     * Retrieve the default text to display when the card is face down
     *
     * @return the text to display when the card is face down - {@code null}
     *     indicates to use the default
     */
    public static String getDefaultFaceDownText()
        {

        return defaultFaceDownText ;

        }  // end getDefaultFaceDownText()


    /**
     * Retrieve the default orientation (face up/down) for new cards
     *
     * @return the current default state (face up/down)
     */
    public static Orientation getDefaultOrientation()
        {

        return defaultOrientation ;

        }  // end getDefaultOrientation()


    /**
     * retrieve the whether new cards will be permanent, temporary, or template
     *
     * @return the current default persistence
     *
     * @since 2.0
     */
    public static Persistence getDefaultPersistence()
        {

        return defaultPersistence ;

        }   // end getDefaultPersistence()


    /**
     * Retrieve the text to display when the card is face up
     *
     * @return the text to display when the card is face up
     *
     * @since 2.0
     */
    public String getFaceUpText()
        {

        return this.faceUpText ;

        }  // end getFaceUpText()


    /**
     * Retrieve the text to display when the card is face down
     *
     * @return the text to display when the card is face down - {@code null}
     *     indicates to use the default
     */
    public String getFaceDownText()
        {

        return this.faceDownText ;

        }  // end getFaceDownText()


    /**
     * Retrieve a card's orientation (face up/down)
     *
     * @return the card's state (face up/down)
     */
    public Orientation getOrientation()
        {

        return this.orientation ;

        }  // end getOrientation()


    /**
     * retrieve the decoration text for permanent cards
     *
     * @return 2-element array where [0] is the left decoration and [1] is the
     *     right
     *
     * @since 2.0
     */
    public static String[] getPermanentDecoration()
        {

        return Arrays.copyOf( defaultPermanentCardDecoration, defaultPermanentCardDecoration.length ) ;

        }   // end getPermanentDecoration()


    /**
     * retrieve the decoration text for template cards
     *
     * @return 2-element array where [0] is the left decoration and [1] is the
     *     right
     *
     * @since 3.2
     */
    public static String[] getTemplateDecoration()
        {

        return Arrays.copyOf( defaultTemplateCardDecoration, defaultTemplateCardDecoration.length ) ;

        }   // end getTemplateDecoration()


    /**
     * retrieve the decoration text for temporary cards
     *
     * @return 2-element array where [0] is the left decoration and [1] is the
     *     right
     *
     * @since 2.0
     */
    public static String[] getTemporaryDecoration()
        {

        return Arrays.copyOf( defaultTemporaryCardDecoration, defaultTemporaryCardDecoration.length ) ;

        }   // end getTemporaryDecoration()


    /**
     * retrieve the whether this card is permanent or temporary
     *
     * @return the card's persistence
     *
     * @since 2.0
     */
    public Persistence getPersistence()
        {

        return this.persistence ;

        }   // end getPersistence()


    /**
     * Turn a card face down
     *
     * @return this card (fluent)
     */
    public CardBase hide()
        {

        this.orientation = FACE_DOWN ;

        return this ;

        }  // end hide()


    /**
     * Compare two cards to see if they match, which may be different from them
     * being {@code equal()}
     * <p>
     * The game or {@code Pile} class may require this method to behave
     * differently from {@code equals()} or {@code compareTo()} which will be
     * implemented in the game-specific Card class.
     * <p>
     * Note: this implementation uses {@code equals()} to perform the match
     * functionality which may be overridden in a subclass
     *
     * @param otherCard
     *     the card to compare against this card
     *
     * @return {@code true} if the cards match, {@code false} otherwise
     */
    public boolean matches( final CardBase otherCard )
        {

        // delegate to equals() - game-specific card should override if
        // necessary
        return equals( otherCard ) ;

        }  // end matches()


    /**
     * reset the default text to display when the card is face down to the
     * system default
     *
     * @return the previous text to display when the card is face down
     */
    public static String resetDefaultFaceDownText()
        {

        final String savedFaceDownText = defaultFaceDownText ;

        defaultFaceDownText = DEFAULT_FACEDOWN_TEXT ;

        return savedFaceDownText ;

        }  // end resetDefaultFaceDownText()


    /**
     * reset the default orientation for new cards to the system default
     * <p>
     * Note that this will only be effective for cards instantiated
     *
     * @return the previous state (face up/down)
     */
    public static Orientation resetDefaultOrientation()
        {

        final Orientation savedDefaultOrientation = defaultOrientation ;

        CardBase.defaultOrientation = DEFAULT_ORIENTATION ;

        return savedDefaultOrientation ;

        }  // end resetDefaultOrientation()


    /**
     * reset the default persistence for new cards to the system default
     * <p>
     * Note that this will only be effective for cards subsequently instantiated
     *
     * @return the previous state (permanent/temporary/template)
     *
     * @since 2.0
     */
    public static Persistence resetDefaultPersistence()
        {

        final Persistence savedDefaultPersistence = defaultPersistence ;

        CardBase.defaultPersistence = DEFAULT_PERSISTENCE ;

        return savedDefaultPersistence ;

        }  // end resetDefaultPersistence()


    /**
     * reset the text to display when the card is face down to the current
     * default
     *
     * @return this card (fluent)
     */
    public CardBase resetFaceDownText()
        {

        this.faceDownText = defaultFaceDownText ;

        return this ;

        }  // end resetFaceDownText()


    /**
     * Reset a card's orientation (face up/down) to the current default
     *
     * @return this card (fluent)
     */
    public CardBase resetOrientation()
        {

        this.orientation = defaultOrientation ;

        return this ;

        }  // end resetOrientation()


    /**
     * Turn a card face up
     *
     * @return this card (fluent)
     */
    public CardBase reveal()
        {

        this.orientation = FACE_UP ;

        return this ;

        }  // end reveal()


    /**
     * Set the default text to display when the card is face down
     *
     * @param newFaceDownText
     *     the new default text to display when the card is face down
     *
     * @return the previous text to display when the card is face down
     */
    public static String setDefaultFaceDownText( final String newFaceDownText )
        {

        final String savedFaceDownText = defaultFaceDownText ;

        defaultFaceDownText = newFaceDownText ;

        return savedFaceDownText ;

        }  // end setDefaultFaceDownText()


    /**
     * Set the default orientation for new cards to be face up/down
     * <p>
     * Note that this will only be effective for cards subsequently instantiated
     *
     * @param newOrientation
     *     the new default orientation (face up/down state)
     *
     * @return the previous state (face up/down)
     *
     * @throws IllegalArgumentException
     *     if the specified orientation is other than {@code FACE_UP} or
     *     {@code FACE_DOWN}
     */
    public static Orientation setDefaultOrientation( final Orientation newOrientation )
            throws IllegalArgumentException
        {

        Objects.requireNonNull( newOrientation, "newOrientation" ) ;

        if ( ( newOrientation != FACE_DOWN ) && ( newOrientation != FACE_UP ) )
            {
            throw new IllegalArgumentException() ;
            }

        final Orientation savedDefaultOrientation = defaultOrientation ;

        CardBase.defaultOrientation = newOrientation ;

        return savedDefaultOrientation ;

        }  // end setDefaultOrientation()


    /**
     * set persistence for new cards
     * <p>
     * Note: decks (the classes which instantiate the cards for game play) are
     * the only classes expected to change the persistence
     *
     * @param newPersistence
     *     the new persistence setting
     *
     * @return the prior persistence
     *
     * @since 2.0
     */
    public static Persistence setDefaultPersistence( final Persistence newPersistence )
        {

        Objects.requireNonNull( newPersistence, "newPersistence" ) ;

        validatePersistence( newPersistence ) ;

        final Persistence savedPersistence = defaultPersistence ;

        defaultPersistence = newPersistence ;

        return savedPersistence ;

        }   // end setDefaultPersistence()


    /**
     * ensure that a persistence is supplied and applicable to a card (is one of
     * {@code TEMPORARY}, {@code PERMANENT}), or {@code TEMPLATE}
     *
     * @param aPersistence
     *     the {@link Persistence} to validate
     *
     * @since 2.0
     */
    private static void validatePersistence( final Persistence aPersistence )
        {

        Objects.requireNonNull( aPersistence, "aPersistence" ) ;

        if ( ( aPersistence != PERMANENT ) && ( aPersistence != TEMPORARY ) && ( aPersistence != TEMPLATE ) )
            {
            throw new IllegalArgumentException( String.format( "%s is not an acceptable persistence for a card",
                                                               aPersistence.getClass().getSimpleName() ) ) ;
            }

        }   // end validatePersistence()


    /**
     * convenience method to set the orientation to face down
     *
     * @return this card (fluent)
     *
     * @since 2.0
     */
    public CardBase setFaceDown()
        {

        setOrientation( FACE_DOWN ) ;

        return this ;

        }   // end setFaceDown()


    /**
     * Set the text to display when the card is face down
     *
     * @param newFaceDownText
     *     the new text to display when the card is face down
     *
     * @return this card (fluent)
     */
    public CardBase setFaceDownText( final String newFaceDownText )
        {

        Objects.requireNonNull( newFaceDownText, "newFaceDownText" ) ;

        this.faceDownText = newFaceDownText ;

        return this ;

        }  // end setFaceDownText()


    /**
     * convenience method to set the orientation to face up
     *
     * @return this card (fluent)
     *
     * @since 2.0
     */
    public CardBase setFaceUp()
        {

        setOrientation( FACE_UP ) ;

        return this ;

        }   // end setFaceUp()


    /**
     * Set the text to display when the card is face up
     *
     * @param newFaceUpText
     *     the new text to display when the card is face up
     *
     * @return this card (fluent)
     */
    public CardBase setFaceUpText( final String newFaceUpText )
        {

        Objects.requireNonNull( newFaceUpText, "newFaceUpText" ) ;

        // cards are immutable wrt their face
        if ( this.faceUpTextLocked )
            {
            throw new IllegalStateException( String.format( "can't change face of card from '%s' to '%s'",
                                                            this.faceUpText,
                                                            newFaceUpText ) ) ;
            }

        this.faceUpText = newFaceUpText ;

        // prevent subsequent changes
        this.faceUpTextLocked = true ;

        return this ;

        }  // end setFaceUpText()


    /**
     * Set a card's orientation (face up/down)
     *
     * @param newOrientation
     *     the new orientation (face up/down) state
     *
     * @return this card (fluent)
     */
    public CardBase setOrientation( final Orientation newOrientation )
        {

        Objects.requireNonNull( newOrientation, "newOrientation" ) ;

        // only FACE_UP and FACE_DOWN are defined for cards
        if ( ( newOrientation != FACE_UP ) && ( newOrientation != FACE_DOWN ) )
            {
            throw new IllegalArgumentException( String.format( "%s is not acceptable for a card",
                                                               newOrientation.name() ) ) ;
            }

        // we have an acceptable orientation
        this.orientation = newOrientation ;

        return this ;

        }  // end setOrientation()


    /**
     * turn inclusion of decoration off
     *
     * @return the prior setting
     *
     * @since 3.2
     */
    public static EnabledDisabled disableDecoration()
        {

        final EnabledDisabled savedEnabledDisabled = includeDecoration ;

        includeDecoration = EnabledDisabled.DISABLED ;

        return savedEnabledDisabled ;

        }   // end disableDecoration()


    /**
     * turn inclusion of decoration on
     *
     * @return the prior setting
     *
     * @since 3.2
     */
    public static EnabledDisabled enableDecoration()
        {

        final EnabledDisabled savedEnabledDisabled = includeDecoration ;

        includeDecoration = EnabledDisabled.ENABLED ;

        return savedEnabledDisabled ;

        }   // end enableDecoration()


    /**
     * set the decoration text for permanent cards where [0] is the left
     * decoration and [1] is the right
     *
     * @param newPermanentDecoration
     *     a 2-element array containing the decoration text
     *
     * @return the prior setting
     *
     * @since 2.0
     */
    public static String[] setPermanentDecoration( final String[] newPermanentDecoration )
        {

        // the array must be non-null, length 2, and contain non-null references
        Objects.requireNonNull( newPermanentDecoration, "newPermanentDecoration" ) ;

        if ( newPermanentDecoration.length != 2 )
            {
            throw new IllegalArgumentException( String.format( "setPermanentDecoration() requires a 2-element array, argument array has %,d element(s)",
                                                               newPermanentDecoration.length ) ) ;
            }

        Objects.requireNonNull( newPermanentDecoration[ 0 ], "newPermanentDecoration[ 0 ]" ) ;
        Objects.requireNonNull( newPermanentDecoration[ 1 ], "newPermanentDecoration[ 1 ]" ) ;

        final String[] priorDecoration = defaultPermanentCardDecoration ;

        // make a copy of the array for safety
        defaultPermanentCardDecoration =
                Arrays.copyOf( newPermanentDecoration, newPermanentDecoration.length ) ;

        return priorDecoration ;

        }   // end setPermanentDecoration()


    /**
     * set the decoration text for template cards where [0] is the left
     * decoration and [1] is the right
     *
     * @param newTemplateDecoration
     *     a 2-element array containing the decoration text
     *
     * @return the prior setting
     *
     * @since 3.2
     */
    public static String[] setTemplateDecoration( final String[] newTemplateDecoration )
        {

        // the array must be non-null, length 2, and contain non-null references
        Objects.requireNonNull( newTemplateDecoration, "newTemplateDecoration" ) ;

        if ( newTemplateDecoration.length != 2 )
            {
            throw new IllegalArgumentException( String.format( "setTemplateDecoration() requires a 2-element array, argument array has %,d element(s)",
                                                               newTemplateDecoration.length ) ) ;
            }

        Objects.requireNonNull( newTemplateDecoration[ 0 ], "newTemplateDecoration[ 0 ]" ) ;
        Objects.requireNonNull( newTemplateDecoration[ 1 ], "newTemplateDecoration[ 1 ]" ) ;

        final String[] priorDecoration = defaultTemplateCardDecoration ;

        // make a copy of the array for safety
        defaultTemplateCardDecoration = Arrays.copyOf( newTemplateDecoration, newTemplateDecoration.length ) ;

        return priorDecoration ;

        }   // end setTemplateDecoration()


    /**
     * set the decoration text for temporary cards where [0] is the left
     * decoration and [1] is the right
     *
     * @param newTemporaryDecoration
     *     a 2-element array containing the decoration text
     *
     * @return the prior setting
     *
     * @since 2.0
     */
    public static String[] setTemporaryDecoration( final String[] newTemporaryDecoration )
        {

        // the array must be non-null, length 2, and contain non-null references
        Objects.requireNonNull( newTemporaryDecoration, "newTemporaryDecoration" ) ;

        if ( newTemporaryDecoration.length != 2 )
            {
            throw new IllegalArgumentException( String.format( "setTemporaryDecoration() requires a 2-element array, argument array has %,d element(s)",
                                                               newTemporaryDecoration.length ) ) ;
            }

        Objects.requireNonNull( newTemporaryDecoration[ 0 ], "newTemporaryDecoration[ 0 ]" ) ;
        Objects.requireNonNull( newTemporaryDecoration[ 1 ], "newTemporaryDecoration[ 1 ]" ) ;

        final String[] priorDecoration = defaultTemporaryCardDecoration ;

        // make a copy of the array for safety
        defaultTemporaryCardDecoration =
                Arrays.copyOf( newTemporaryDecoration, newTemporaryDecoration.length ) ;

        return priorDecoration ;

        }   // end setTemporaryDecoration()


    @Override
    public String toString()
        {

        return String.format( "%s%s%s",
                              includeDecoration == EnabledDisabled.ENABLED
                                      ? this.decoration[ 0 ]
                                      : "",
                              this.orientation == FACE_UP
                                      ? this.faceUpText
                                      : this.faceDownText,
                              includeDecoration == EnabledDisabled.ENABLED
                                      ? this.decoration[ 1 ]
                                      : "" ) ;

        }   // end toString()


    /*
     * protected and private utility methods
     */


    /**
     * retrieve the face up text lock setting
     *
     * @return {@code true} if locked, {@code false} if not locked (changeable)
     */
    protected boolean getFaceUpTextLocked()
        {

        return this.faceUpTextLocked ;

        }   // end getFaceUpTextLocked()


    /**
     * set the decoration according to the persistence
     */
    private void setDecoration()
        {

        System.arraycopy( switch ( this.persistence )
            {
            case Persistence.PERMANENT
                -> defaultPermanentCardDecoration ;
            case Persistence.TEMPLATE
                -> defaultTemplateCardDecoration ;
            case Persistence.TEMPORARY
                -> defaultTemporaryCardDecoration ;
            default
                -> throw new IllegalStateException( this.persistence.toString() ) ;
            }, 0, this.decoration, 0, this.decoration.length ) ;

        }   // end setDecoration()


    /**
     * set the face up text lock setting
     *
     * @param locked
     *     {@code true} to lock it, {@code false} to make it changeable
     *
     * @return the prior setting
     */
    protected boolean setFaceUpTextLocked( final boolean locked )
        {

        final boolean wasLocked = this.faceUpTextLocked ;

        this.faceUpTextLocked = locked ;

        return wasLocked ;

        }   // end setFaceUpTextLocked()


    /*
     * for testing and debugging
     */
    // none


    /*
     * utility classes
     */
    // none

    }   // end class CardBase