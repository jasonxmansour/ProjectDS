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


package edu.wit.scds.ds.lists.app.card_game.standard_cards.pile ;

import edu.wit.scds.ds.lists.app.card_game.standard_cards.card.Card ;
import edu.wit.scds.ds.lists.app.card_game.standard_cards.card.Rank ;
import edu.wit.scds.ds.lists.app.card_game.standard_cards.card.Suit ;
import edu.wit.scds.ds.lists.app.card_game.universal_base.card.CardBase ;
import edu.wit.scds.ds.lists.app.card_game.universal_base.pile.PileBase ;
import edu.wit.scds.ds.lists.app.card_game.universal_base.support.Orientation ;

import static edu.wit.scds.ds.lists.app.card_game.standard_cards.card.Card.newCardLike ;
import static edu.wit.scds.ds.lists.app.card_game.universal_base.support.Orientation.FACE_DOWN ;

import java.util.Iterator ;
import java.util.ListIterator ;


// you probably will not modify this code - talk to me first

/**
 * Representation of a pile of standard playing cards
 * <p>
 * the top card is at position 0
 *
 * @author Dave Rosenberg
 *
 * @version 1.0 2025-06-26 extracted standard playing card-specific methods from
 *     {@code Pile}
 * @version 2.0 2025-07-11
 *     <ul>
 *     <li>track changes to previous {@code common} package, now
 *     {@code universal_base}
 *     <li>rename from {@code StandardPile} to {@code Pile}
 *     </ul>
 * @version 2.1 2025-11-19 extend functionality to support deck validation
 */
public abstract class Pile extends PileBase
    {

    /*
     * utility constants
     */


    /** by default, cards added to this pile will be turned face down */
    private final static Orientation DEFAULT_CARD_ORIENTATION = FACE_DOWN ;


    /*
     * data fields
     */
    // none


    /*
     * constructors
     */


    /**
     * Initialize the pile with cards placed face down by default
     */
    protected Pile()
        {

        this( DEFAULT_CARD_ORIENTATION ) ;

        }	// end no-arg constructor


    /**
     * Initialize the pile with cards placed face up/down as specified by
     * default
     *
     * @param initialOrientation
     *     specify whether cards will be face up or down by default
     */
    protected Pile( final Orientation initialOrientation )
        {

        super( initialOrientation ) ;

        }	// end 1-arg constructor


    /*
     * public methods
     */


    @Override
    public Card getCardLike( final CardBase likeCard )
        {

        return (Card) super.getCardLike( likeCard ) ;

        }  // end getCardLike() given a Card


    /**
     * Retrieve a specific card from the pile by card - the card is not removed
     * from the pile
     *
     * @param rank
     *     the rank of the desired card
     * @param suit
     *     the suit of the desired card
     *
     * @return the specified card or {@code null} if the card isn't in the pile
     */
    public Card getCardLike( final Rank rank,
                             final Suit suit )
        {

        return getCardLike( newCardLike( rank, suit ) ) ;

        }  // end getCardLike() given rank and suit


    @Override
    public Iterator<CardBase> iterator()
        {

        return new CardIterator() ;

        }   // end iterator()


    /**
     * creates a list iterator for this pile
     *
     * @return a new {@code ListIterator}
     */
    @Override
    public ListIterator<CardBase> listIterator()
        {

        return new CardIterator() ;

        }   // end listIterator()


    /**
     * Remove all cards from the pile
     *
     * @return a new pile containing the removed cards, if any
     */
    public Pile removeAllCards()
        {

        final Pile allCards = new Pile() {} ;

        for ( final CardBase card : super.removeAll() )
            {
            allCards.addToBottom( card ) ;
            }

        return allCards ;

        }  // end removeAllCards()


    /**
     * Remove all instances of a specific card from the pile
     *
     * @param lookupCard
     *     the card to be removed
     *
     * @return a new pile containing the removed cards, if any
     */
    public Pile removeAllMatchingCards( final Card lookupCard )
        {

        final Pile removedCards = new Pile() {} ;

        super.removeAllMatchingCards( lookupCard, removedCards ) ;

        return removedCards ;

        }  // end removeAllMatchingCards()


    @Override
    public Card removeCard( final CardBase card )
        {

        return (Card) super.removeCard( card ) ;

        }  // end removeCard() given a card


    /**
     * Remove a specific card from the pile by card
     *
     * @param rank
     *     rank of the card to be removed
     * @param suit
     *     suit of the card to be removed
     *
     * @return the specified card or {@code null} if the card isn't in the pile
     */
    public Card removeCard( final Rank rank,
                            final Suit suit )
        {

        return removeCard( newCardLike( rank, suit ) ) ;

        }  // end removeCard() given a suit and rank


    @Override
    public Card removeCardAt( final int position )
        {

        return (Card) super.removeCardAt( position ) ;

        }  // end removeCardAt()


    @Override
    public Card removeTopCard()
        {

        return (Card) super.removeTopCard() ;

        }  // end removeTopCard()


    @Override
    public Pile setDefaultFaceDown()
        {

        super.setDefaultFaceDown() ;

        return this ;

        }   // end setFaceDown()


    @Override
    public Pile setDefaultFaceUp()
        {

        super.setDefaultFaceUp() ;

        return this ;

        }   // end setFaceUp()


    /*
     * utility methods
     */
    // none


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
    private class CardIterator implements ListIterator<CardBase>
        {

        /** the actual iterator is that of the PileBase */
        private final ListIterator<CardBase> cardIterator ;


        /**
         * configure the instance state
         */
        private CardIterator()
            {

            this.cardIterator = Pile.super.listIterator() ;

            }   // end constructor


        @Override
        public boolean hasNext()
            {

            return this.cardIterator.hasNext() ;

            }   // end hasNext()


        @Override
        public Card next()
            {

            return (Card) this.cardIterator.next() ;

            }   // end next()


        @Override
        public boolean hasPrevious()
            {

            return this.cardIterator.hasPrevious() ;

            }   // end hasPrevious()


        @Override
        public Card previous()
            {

            return (Card) this.cardIterator.previous() ;

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

        }   // end inner class CardIterator

    }	// end class Pile