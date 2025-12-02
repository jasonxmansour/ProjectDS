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
 * Runtime exception to wrap lower-level exceptions during testing - uses
 * Exception chaining
 *
 * @author Dave Rosenberg
 *
 * @version 1.0 2023-03-25 Initial implementation (based on
 *     {@code TestingException})
 */
public final class NoCardsException extends RuntimeException
    {

    /**
     * Support serialization
     */
    private static final long serialVersionUID = 1L ;


    /*
     * constructors
     */


    /**
     * bare bones exception
     */
    public NoCardsException()
        {

        super() ;

        }   // end no-arg constructor


    /*
     * public methods
     */


    /**
     * exception with message
     *
     * @param message
     *     the message text associated with this exception
     */
    public NoCardsException( final String message )
        {

        super( message ) ;

        }   // end constructor with descriptive message


    /**
     * exception resulting from another exception
     *
     * @param cause
     *     the 'wrapped' exception
     */
    public NoCardsException( final Throwable cause )
        {

        super( cause ) ;

        }   // end 'simple wrapper' constructor without message


    /**
     * exception with message resulting from another exception
     *
     * @param message
     *     descriptive message related to the {@code cause}
     * @param cause
     *     the 'wrapped' exception
     */
    public NoCardsException( final String message,
                             final Throwable cause )
        {

        super( message, cause ) ;

        }   // end 'wrapper' constructor with descriptive message

    }   // end class NoCardsException