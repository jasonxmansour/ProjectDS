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


package edu.wit.scds.ds.lists.app.card_game.universal_base.pile ;

import edu.wit.scds.ds.lists.app.card_game.universal_base.card.CardBase ;
import edu.wit.scds.ds.lists.app.card_game.universal_base.support.NoCardsException ;
import edu.wit.scds.ds.lists.app.card_game.universal_base.support.Orientation ;
import edu.wit.scds.ds.lists.app.card_game.universal_base.support.Persistence ;

import static edu.wit.scds.ds.lists.app.card_game.universal_base.support.Orientation.AS_IS ;
import static edu.wit.scds.ds.lists.app.card_game.universal_base.support.Persistence.PERMANENT ;
import static edu.wit.scds.ds.lists.app.card_game.universal_base.support.Persistence.UNRESTRICTED ;

import java.util.Collections ;
import java.util.Iterator ;
import java.util.LinkedList ;
import java.util.List ;
import java.util.ListIterator ;
import java.util.Objects ;


// do not modify this code

/**
 * Representation of a pile of cards
 * <p>
 * the top card is at position 0
 *
 * @author Dave Rosenberg
 *
 * @version 1.0 2021-12-08 Initial implementation
 * @version 2.0 2025-03-26
 *     <ul>
 *     <li>change to {@code abstract} class so games must be built with
 *     appropriate subclasses of {@code Pile}
 *     <li>remove no-arg constructor
 *     </ul>
 * @version 3.0 2025-03-30
 *     <ul>
 *     <li>switch from {@code ArrayList} to {@code LinkedList} to improve
 *     efficiency to add/remove at both ends of the list (top and bottom) with
 *     O(1) efficiency
 *     <li>remove initial capacity argument from constructor - no longer
 *     needed/relevant
 *     <li>major cleanup and restructuring
 *     <li>move {@code shuffle()} here from {@code Deck}
 *     <li>support iteration
 *     </ul>
 * @version 4.0 2025-06-26
 *     <ul>
 *     <li>track changes to other classes to support different types of card
 *     decks
 *     <li>move standard playing deck-specific methods to {@code Pile}
 *     <li>add various constructors and methods
 *     <li>make most instance methods fluent
 *     </ul>
 * @version 5.0 2025-07-12
 *     <ul>
 *     <li>rename from {@code Pile} to {@code UniversalBasePile} for consistency
 *     with {@code Card}
 *     <li>add {@code null}-argument checking
 *     <li>add methods to manipulate the default orientation
 *     <li>support {@code AS-IS} default orientation
 *     <li>rename some methods to improve consistency
 *     <li>add tooling to optionally restrict cards based upon their persistence
 *     </ul>
 * @version 6.0 2025-11-02 rename from {@code UniversalBasePile} to
 *     {@code PileBase} - less of a mouthful
 * @version 6.1 2025-11-19
 *     <ul>
 *     <li>add template card support
 *     <li>prevent clearing a pile unless it only contains temporary cards
 *     </ul>
 * @version 6.2 2025-11-19 add support for verification of decks that they have
 *     the correct contents, typically at the end of a game to determine if any
 *     cards have been lost
 */
public abstract class PileBase implements Iterable<CardBase>
    {

    /*
     * constants
     */


    /**
     * by default, leave the orientation for {@code Card}s added to us unchanged
     */
    protected final static Orientation DEFAULT_ORIENTATION = AS_IS ;

    /** by default, piles will only accept permanent cards */
    protected final static Persistence DEFAULT_ACCEPTABLE_CARD_PERSISTENCE = PERMANENT ;


    /*
     * data fields
     */


    /**
     * the list of cards where the top is in position 0 and the bottom is the
     * highest position
     */
    protected List<CardBase> cards ;

    /**
     * indicate whether added cards should be face up or down or left as-is by
     * default
     */
    protected Orientation defaultOrientation ;

    /**
     * restrict to permanent, template, temporary, or accept any - can only be
     * set during instantiation
     */
    private Persistence acceptablePersistence ;


    /*
     * constructors
     */


    /**
     * Initialize the pile with default configuration
     *
     * @since 3.0
     */
    protected PileBase()
        {

        this.cards = new LinkedList<>() ;

        resetDefaultOrientation() ;

        setAcceptablePersistence( DEFAULT_ACCEPTABLE_CARD_PERSISTENCE ) ;

        }   // end no-arg constructor


    /**
     * Initialize the pile with added cards placed face up/down/as-is by default
     *
     * @param initialOrientation
     *     specify the default orientation for newly added cards
     *
     * @since 3.0
     */
    protected PileBase( final Orientation initialOrientation )
        {

        this() ;

        setDefaultOrientation( initialOrientation ) ;

        }   // end 1-arg constructor w/ orientation


    /**
     * initialize this pile with added cards required to be compatible with the
     * specified persistence
     *
     * @param persistenceSetting
     *     specify whether this pile will only accept permanent, template,
     *     temporary, or any cards
     *
     * @since 3.0
     */
    protected PileBase( final Persistence persistenceSetting )
        {

        this() ;

        setAcceptablePersistence( persistenceSetting ) ;

        }   // end 1-arg constructor w/ persistence


    /**
     * initialize the pile and populate it with provide {@code Card}s
     *
     * @param sourcePile
     *     if non-{@code null}, any {@code Card}s it contains will be moved to
     *     this {@code Pile}
     *
     * @since 4.0
     */
    protected PileBase( final PileBase sourcePile )
        {

        this() ;

        if ( sourcePile != null )
            {
            moveCardsToBottom( sourcePile ) ;
            }

        }   // end 1-arg constructor w/ source pile


    /**
     * initialize the pile and populate it with provided {@code Card}s
     *
     * @param sourcePile
     *     if non-{@code null}, any {@code Card}s it contains will be moved to
     *     this {@code Pile}
     * @param initialOrientation
     *     specify whether cards will be face up or down by default
     *
     * @since 4.0
     */
    protected PileBase( final PileBase sourcePile,
                        final Orientation initialOrientation )
        {

        this( initialOrientation ) ;    // checks for null argument

        if ( sourcePile != null )
            {
            moveCardsToBottom( sourcePile ) ;
            }

        }   // end 2-arg constructor w/ source pile and orientation


    /*
     * public methods
     */


    /**
     * Add a single card at a specified position
     *
     * @param newCard
     *     the card to add - will be turned face up/down if necessary
     * @param position
     *     the 0-based position for the card where 0 is the top
     *
     * @return this pile (fluent)
     *
     * @throws IndexOutOfBoundsException
     *     if the position is invalid given the current card count
     *
     * @since 3.0
     */
    public PileBase addAtPosition( final CardBase newCard,
                                   final int position )
            throws IndexOutOfBoundsException
        {

        prepareCardToAdd( newCard ) ;   // checks for null argument

        this.cards.add( position, newCard ) ;

        return this ;

        }  // end addAt()


    /**
     * Add a single card to the bottom of the pile
     *
     * @param newCard
     *     the card to add - will be turned face up/down if necessary
     *
     * @return this pile (fluent)
     *
     * @since 3.0
     */
    public PileBase addToBottom( final CardBase newCard )
        {

        prepareCardToAdd( newCard ) ;   // checks for null argument

        this.cards.addLast( newCard ) ;

        return this ;

        }  // end addToBottom()


    /**
     * Add a single card to the top of the pile
     *
     * @param newCard
     *     the card to add - will be turned face up/down if necessary
     *
     * @return this pile (fluent)
     *
     * @since 3.0
     */
    public PileBase addToTop( final CardBase newCard )
        {

        prepareCardToAdd( newCard ) ;   // checks for null argument

        this.cards.addFirst( newCard ) ;

        return this ;

        }  // end addToTop()


    /**
     * Retrieve the current number of cards in the pile
     *
     * @return the current number of cards in the pile
     */
    public int cardCount()
        {

        return this.cards.size() ;

        }  // end cardCount()


    /**
     * remove all cards from the pile
     * <p>
     * use with caution - all cards must eventually be returned to the deck(s)
     *
     * @return this pile (fluent)
     *
     * @since 3.0
     */
    public PileBase clear()
        {

        this.cards.clear() ;

        return this ;

        }   // end clear()


    /**
     * flip all cards in the pile - if a card was face down, turn it face up,
     * and vice versa
     *
     * @return this pile (fluent)
     */
    public PileBase flipAll()
        {

        this.cards.forEach( CardBase::flip ) ;

        return this ;

        }   // end flipAll()


    /**
     * retrieve the persistence restrictions for this pile
     *
     * @return the acceptable card persistence setting
     *
     * @since 5.0
     */
    public Persistence getAcceptablePersistence()
        {

        return this.acceptablePersistence ;

        }   // end getAcceptablePersistence()


    /**
     * retrieve the default orientation for cards added to this pile
     *
     * @return the default orientation
     *
     * @since 4.1
     */
    public Orientation getDefaultOrientation()
        {

        return this.defaultOrientation ;

        }   // end getDefaultOrientation()


    /**
     * Retrieve the bottom card from the pile - the card is not removed from the
     * pile
     * <p>
     * Caution: the returned card should only be in one pile at any time
     *
     * @return the bottom card
     *
     * @throws NoCardsException
     *     if the pile is empty
     *
     * @since 3.0
     */
    public CardBase getBottomCard() throws NoCardsException
        {

        if ( isEmpty() )
            {
            throw new NoCardsException() ;
            }

        // assertion: there is at least one card in the pile

        return this.cards.getLast() ;

        }  // end getTopCard()


    /**
     * Retrieve a specific card from the pile by position - the card is not
     * removed from the pile
     * <p>
     * Caution: the returned card should only be in one pile at any time
     *
     * @param position
     *     the 0-based position of the card in the pile where 0 is the top of
     *     the pile
     *
     * @return the specified card
     *
     * @throws NoCardsException
     *     if the pile is empty
     *
     * @since 3.0
     */
    public CardBase getCardAt( final int position ) throws NoCardsException
        {

        if ( isEmpty() )
            {
            throw new NoCardsException() ;
            }

        // assertion: there is at least one card in the pile

        return this.cards.get( position ) ;

        }  // end getCardAt()


    /**
     * Retrieve a specific card from the pile by card - the card is not removed
     * from the pile
     * <p>
     * Caution: the returned card should only be in one pile at any time
     *
     * @param likeCard
     *     a placeholder - usually is a throw-away instance used for lookup
     *     only, not game play
     *
     * @return the specified card or {@code null} if the card isn't in the pile
     *
     * @since 3.0
     */
    public CardBase getCardLike( final CardBase likeCard )
        {

        Objects.requireNonNull( likeCard, "likeCard" ) ;

        final int positionOfCard = this.cards.indexOf( likeCard ) ;

        if ( positionOfCard == -1 )
            {
            return null ;
            }

        return this.cards.get( positionOfCard ) ;

        }  // end getCardLike() given a Card


    /**
     * Retrieve the top card from the pile - the card is not removed from the
     * pile
     * <p>
     * Caution: the returned card should only be in one pile at any time
     * <p>
     * Note: there is no corresponding convenience method to retrieve the bottom
     * card since that's usually cheating and we can't condone that 8~)
     *
     * @return the top card
     *
     * @throws NoCardsException
     *     if the pile is empty
     *
     * @since 3.0
     */
    public CardBase getTopCard() throws NoCardsException
        {

        if ( isEmpty() )
            {
            throw new NoCardsException() ;
            }

        // assertion: there is at least one card in the pile

        return this.cards.getFirst() ;

        }  // end getTopCard()


    /**
     * turn all cards in the pile face down
     *
     * @return this pile (fluent)
     */
    public PileBase hideAll()
        {

        this.cards.forEach( CardBase::hide ) ;

        return this ;

        }   // end hideAll()


    /**
     * Report if the pile is empty (contains no cards)
     *
     * @return true if there are no cards in the pile; false otherwise
     */
    public boolean isEmpty()
        {

        return this.cards.isEmpty() ;

        }   // end isEmpty()


    @Override
    public Iterator<CardBase> iterator()
        {

        return new CardBaseIterator() ;

        }   // end iterator()


    /**
     * creates a list iterator for this pile
     *
     * @return a new {@code ListIterator}
     *
     * @since 3.0
     */
    public ListIterator<CardBase> listIterator()
        {

        return new CardBaseIterator() ;

        }   // end listIterator()


    /**
     * Counts the number of cards in the pile which match the target card
     * <p>
     * This count may be different than would be determined by testing cards for
     * equality, for instance, if matching only rank or suit.
     *
     * @param targetCard
     *     the card to look for
     *
     * @return the number of cards which match the target card
     */
    public int matchCount( final CardBase targetCard )
        {

        Objects.requireNonNull( targetCard, "targetCard" ) ;

        int matches = 0 ;

        // count the number of times the target card appears in the pile
        for ( final CardBase aCard : this.cards )
            {

            if ( aCard.matches( targetCard ) )
                {
                matches++ ;
                }

            }   // end for

        return matches ;

        }   // end matchCount()


    /**
     * Move all cards from {@code otherCards} to the bottom of this pile
     *
     * @param otherCards
     *     another pile of cards
     *     <p>
     *     post-condition: {@code otherCards} will be empty
     *
     * @return this pile (fluent)
     */
    public PileBase moveCardsToBottom( final PileBase otherCards )
        {

        Objects.requireNonNull( otherCards, "otherCards" ) ;

        // add the cards to the bottom of our pile
        this.cards.addAll( nextBottomPosition(), otherCards.cards ) ;

        // remove all cards from the other pile
        otherCards.clear() ;

        return this ;

        }  // end moveCardsToBottom()


    /**
     * Move all cards from {@code otherCards} to the top of this pile
     * <p>
     * otherCards are placed in their original order on top of the current top
     * of this pile
     *
     * @param otherCards
     *     another pile of cards
     *     <p>
     *     post-condition: {@code otherCards} will be empty
     *
     * @return this pile (fluent)
     */
    public PileBase moveCardsToTop( final PileBase otherCards )
        {

        Objects.requireNonNull( otherCards, "otherCards" ) ;

        // add the cards to the top of our pile
        this.cards.addAll( nextTopPosition(), otherCards.cards ) ;

        // remove all cards from the other pile
        otherCards.clear() ;

        return this ;

        }  // end moveCardsToTop()


    /**
     * Remove a specific card from the pile by card
     *
     * @param card
     *     the card to be removed
     *
     * @return the specified card or {@code null} if the card isn't in the pile
     *
     * @since 3.0
     */
    public CardBase removeCard( final CardBase card )
        {

        Objects.requireNonNull( card, "card" ) ;

        final int positionOfCard = this.cards.indexOf( card ) ;

        if ( positionOfCard == -1 )
            {
            return null ;
            }

        return this.cards.remove( positionOfCard ) ;

        }  // end removeCard()


    /**
     * Remove all cards from the pile
     *
     * @return a new anonymous pile containing all cards from this pile
     *
     * @since 6.2
     */
    public PileBase removeAll()
        {

        // pull all cards from the pile
        final PileBase allRemovedCards = new PileBase() {} ;
        allRemovedCards.setAcceptablePersistence( UNRESTRICTED ).setDefaultOrientation( AS_IS ) ;

        // move (copy then delete all) all the cards from our list into the the
        // temporary pile
        allRemovedCards.cards.addAll( this.cards ) ;
        this.cards.clear() ;

        return allRemovedCards ;

        }  // end removeAll()


    /**
     * Remove all instances of a specific card from the pile
     *
     * @param lookupCard
     *     the card to be removed
     * @param removedCards
     *     an existing {@code PileBase} to receive the removed cards
     *
     * @return this pile (fluent)
     *
     * @since 4.0
     */
    public PileBase removeAllMatchingCards( final CardBase lookupCard,
                                            final PileBase removedCards )
        {

        Objects.requireNonNull( lookupCard, "lookupCard" ) ;
        Objects.requireNonNull( removedCards, "removedCards" ) ;

        // pull any matching cards from the pile
        CardBase foundCard ;

        while ( ( foundCard = removeCard( lookupCard ) ) != null )
            {
            removedCards.addToBottom( foundCard ) ;
            }

        return this ;

        }  // end removeAllMatchingCards()


    /**
     * Remove a specific card from the pile by position
     *
     * @param position
     *     the 0-based position of the card in the pile where 0 is the bottom of
     *     the pile
     *
     * @return the specified card
     *
     * @throws NoCardsException
     *     if the pile is empty
     * @throws IndexOutOfBoundsException
     *     if the specified position is negative or greater than or equal to the
     *     number of cards in the pile
     */
    public CardBase removeCardAt( final int position ) throws NoCardsException, IndexOutOfBoundsException
        {

        if ( isEmpty() )
            {
            throw new NoCardsException() ;
            }

        // assertion: there is at least one card in the pile

        return this.cards.remove( position ) ;

        }  // end removeCardAt()


    /**
     * Remove the top card from the pile
     * <p>
     * Note: there is no corresponding convenience method to remove the bottom
     * card since that's usually cheating and we can't condone that 8~)
     *
     * @return the top card
     *
     * @throws NoCardsException
     *     if the pile is empty
     */
    public CardBase removeTopCard() throws NoCardsException
        {

        if ( isEmpty() )
            {
            throw new NoCardsException() ;
            }

        // assertion: there is at least one card in the pile

        return this.cards.removeFirst() ;

        }  // end removeTopCard()


    /**
     * turn all cards in the pile face up
     *
     * @return this pile (fluent)
     */
    public PileBase revealAll()
        {

        this.cards.forEach( CardBase::reveal ) ;

        return this ;

        }   // end revealAll()


    /**
     * reset the default orientation for cards added to this pile
     *
     * @return this pile (fluent)
     *
     * @since 4.1
     */
    public PileBase resetDefaultOrientation()
        {

        this.defaultOrientation = DEFAULT_ORIENTATION ;

        return this ;

        }   // end resetDefaultOrientation()


    /**
     * set the default orientation for cards added to this pile to face down
     *
     * @return this pile (fluent)
     *
     * @since 5.0
     */
    public PileBase setDefaultFaceDown()
        {

        setDefaultOrientation( Orientation.FACE_DOWN ) ;

        return this ;

        }   // end setDefaultFaceDown()


    /**
     * set the default orientation for cards added to this pile to face up
     *
     * @return this pile (fluent)
     *
     * @since 5.0
     */
    public PileBase setDefaultFaceUp()
        {

        setDefaultOrientation( Orientation.FACE_UP ) ;

        return this ;

        }   // end setDefaultFaceUp()


    /**
     * set the default orientation for cards added to this pile
     *
     * @param newOrientation
     *     the orientation to be used by default for all cards subsequently
     *     added to this pile
     *
     * @return this pile (fluent)
     *
     * @since 4.1
     */
    public PileBase setDefaultOrientation( final Orientation newOrientation )
        {

        Objects.requireNonNull( newOrientation, "newOrientation" ) ;

        this.defaultOrientation = newOrientation ;

        return this ;

        }   // end setDefaultOrientation()


    /**
     * Randomize (shuffle) the cards in the deck
     *
     * @return this pile (fluent)
     *
     * @since 3.0
     */
    public PileBase shuffle()
        {

        Collections.shuffle( this.cards ) ;

        return this ;

        }   // end shuffle()


    /**
     * Reorder (sort) the cards in the deck
     *
     * @return this pile (fluent)
     */
    public PileBase sort()
        {

        Collections.sort( this.cards ) ;

        return this ;

        }   // end sort()


    @Override
    public String toString()
        {

        return this.cards.toString() ;

        }   // end toString()


    /*
     * protected utility methods
     */


    /**
     * determine the position of the card currently on the bottom of the pile
     *
     * @return the position of the current bottom card
     *
     * @since 3.0
     */
    protected int currentBottomPosition()
        {

        return ( this.cards.size() - 1 ) ;

        }   // end currentBottomPosition()


    /**
     * determine the position of the card currently on top of the pile
     *
     * @return the position of the current top card
     *
     * @since 3.0
     */
    @SuppressWarnings( "static-method" )
    protected int currentTopPosition()
        {

        return 0 ;

        }   // end currentTopPosition()


    /**
     * determine the position of a card to be added to the bottom of the pile
     *
     * @return the position of the next bottom card
     *
     * @since 3.0
     */
    protected int nextBottomPosition()
        {

        return this.cards.size() ;

        }   // end nextBottomPosition()


    /**
     * determine the position of a card to be added to the top of the pile
     *
     * @return the position of the next top card
     *
     * @since 3.0
     */
    @SuppressWarnings( "static-method" )
    protected int nextTopPosition()
        {

        return 0 ;

        }   // end nextTopPosition()


    /**
     * set the persistence requirements for this pile (permanent, template,
     * temporary, unrestricted)
     *
     * @param persistenceSetting
     *     a non-{@code null} specification
     *
     * @return this pile (fluent)
     *
     * @since 5.0
     */
    public PileBase setAcceptablePersistence( final Persistence persistenceSetting )
        {

        Objects.requireNonNull( persistenceSetting, "persistenceSetting" ) ;

        this.acceptablePersistence = persistenceSetting ;

        return this ;   // fluent

        }   // end setAcceptablePersistence()


    /*
     * private utility methods
     */


    /**
     * prepare a card to be added to this pile:
     * <ul>
     * <li>must be provided (non-{@code null})
     * <li>must have an acceptable persistence
     * <li>adjust orientation if necessary
     * </ul>
     *
     * @param newCard
     *     the card to be added
     *
     * @since 5.0
     */
    private void prepareCardToAdd( final CardBase newCard )
        {

        // new card is required
        Objects.requireNonNull( newCard, "newCard" ) ;

        // check persistence
        // card's persistence must match ours
        if ( ( this.acceptablePersistence != UNRESTRICTED )
             && ( this.acceptablePersistence != newCard.getPersistence() ) )
            {
            throw new IllegalArgumentException( String.format( "%s only accepting %s cards given a %s card",
                                                               this.getClass().getSimpleName(),
                                                               this.acceptablePersistence,
                                                               newCard.getPersistence() ) ) ;
            }

        // set orientation if necessary
        if ( this.defaultOrientation != AS_IS )
            {
            newCard.setOrientation( this.defaultOrientation ) ;
            }

        }   // end prepareCardToAdd()


    /*
     * testing/debugging
     */
    // none


    /*
     * utility classes
     */


    /**
     * enable iteration over all cards in the pile
     */
    private class CardBaseIterator implements ListIterator<CardBase>
        {

        /** the actual iterator is that of the list of cards */
        private final ListIterator<CardBase> cardIterator ;


        /**
         * configure the instance state
         */
        private CardBaseIterator()
            {

            this.cardIterator = PileBase.this.cards.listIterator() ;

            }   // end constructor


        @Override
        public boolean hasNext()
            {

            return this.cardIterator.hasNext() ;

            }   // end hasNext()


        @Override
        public CardBase next()
            {

            return this.cardIterator.next() ;

            }   // end next()


        @Override
        public boolean hasPrevious()
            {

            return this.cardIterator.hasPrevious() ;

            }   // end hasPrevious()


        @Override
        public CardBase previous()
            {

            return this.cardIterator.previous() ;

            }   // end previous()


        @Override
        public int nextIndex()
            {

            return this.cardIterator.nextIndex() ;

            }   // end nextIndex()


        @Override
        public int previousIndex()
            {

            return this.cardIterator.previousIndex() ;

            }   // end previousIndex()


        @Override
        public void remove()
            {

            this.cardIterator.remove() ;

            }   // end remove()


        @Override
        public void set( final CardBase replacementCard )
            {

            this.cardIterator.set( replacementCard ) ;

            }   // end set()


        @Override
        public void add( final CardBase newCard )
            {

            this.cardIterator.add( newCard ) ;

            }   // end add()

        }   // end inner class CardBaseIterator

    }   // end class PileBase