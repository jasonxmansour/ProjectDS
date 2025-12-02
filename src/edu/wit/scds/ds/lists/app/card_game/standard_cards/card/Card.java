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

import edu.wit.scds.ds.lists.app.card_game.universal_base.card.CardBase ;
import edu.wit.scds.ds.lists.app.card_game.universal_base.support.Persistence ;

import static edu.wit.scds.ds.lists.app.card_game.standard_cards.card.Rank.FOUR ;
import static edu.wit.scds.ds.lists.app.card_game.standard_cards.card.Rank.JOKER ;
import static edu.wit.scds.ds.lists.app.card_game.standard_cards.card.Rank.KING ;
import static edu.wit.scds.ds.lists.app.card_game.standard_cards.card.Rank.QUEEN ;
import static edu.wit.scds.ds.lists.app.card_game.standard_cards.card.Suit.DIAMONDS ;
import static edu.wit.scds.ds.lists.app.card_game.standard_cards.card.Suit.HEARTS ;
import static edu.wit.scds.ds.lists.app.card_game.standard_cards.card.Suit.NA ;
import static edu.wit.scds.ds.lists.app.card_game.standard_cards.card.Suit.SPADES ;
import static edu.wit.scds.ds.lists.app.card_game.universal_base.support.Orientation.FACE_DOWN ;
import static edu.wit.scds.ds.lists.app.card_game.universal_base.support.Orientation.FACE_UP ;
import static edu.wit.scds.ds.lists.app.card_game.universal_base.support.Persistence.TEMPLATE ;
import static edu.wit.scds.ds.lists.app.card_game.universal_base.support.Persistence.TEMPORARY ;

import java.util.ArrayList ;
import java.util.Collections ;
import java.util.List ;
import java.util.Objects ;

// you probably will not modify this code - talk to me first

/**
 * Representation of a standard playing card with a suit and rank
 * <p>
 * The suit and rank are immutable.
 * <p>
 * Note: we will override all superclass methods that our game uses which return
 * a {@link CardBase} reference to reduce/eliminate the need to cast to
 * {@code Card}, particularly for fluent methods
 *
 * @author Dave Rosenberg
 *
 * @version 1.0 2020-11-19 initial version
 * @version 2.0 2021-12-08
 *     <ul>
 *     <li>add support for face up/down
 *     <li>add {@code matches()}
 *     </ul>
 * @version 2.1 2022-11-06 support dynamic switching to compare cards based on
 *     suit and rank or rank alone
 * @version 2.2 2024-03-26
 *     <ul>
 *     <li>minor cosmetic changes
 *     <li>revise {@code toString()} for greater formatting flexibility and
 *     control
 *     </ul>
 * @version 3.0 2025-03-25 track changes to {@code Suit} and addition of
 *     {@code Color}
 * @version 4.0 2025-03-30 switch comparison from suit then rank to rank then
 *     suit
 * @version 5.0 2025-06-26
 *     <ul>
 *     <li>make subclass of {@code Card} to support different kinds of cards
 *     <li>move general constants, fields, and methods to {@code Card}
 *     <li>track changes to other {@code Card}-related classes
 *     </ul>
 * @version 6.0 2025-07-11
 *     <ul>
 *     <li>track changes to {@code UniversalBaseCard} fka {@code Card}
 *     <li>rename this class from {@code StandardCard} to {@code Card}
 *     <li>eliminate our {@code toString()} because {@code UniversalBaseCard}'s
 *     was re-tooled to do the work
 *     <li>add {@code null}-argument checking
 *     <li>override fluent methods used in our game
 *     </ul>
 * @version 7.0 2025-07-13 remove all inner class {@code Comparator}s
 * @version 8.0 2025-11-04 change comparison control from
 *     {@code true}/{@code false} to an {@code enum} to support more
 *     combinations of comparison including disabling them
 * @version 8.1 2025-11-19 add support for template cards
 */
public final class Card extends CardBase
    {

    /*
     * utility constants
     */


    /**
     * specifiers for which attributes of a {@code Card} should be used when
     * comparing instances
     */
    public enum CompareOn
        {

        // @formatter:off

           /** for comparison operations: equals, matches, compareTo: only consider suit */
         COMPARE_SUIT_ONLY

         , /** for comparison operations: equals, matches, compareTo: only consider rank */
         COMPARE_RANK_ONLY

         , /** for comparison operations: equals, matches, compareTo: consider both suit and rank */
         COMPARE_SUIT_AND_RANK

         , /** effectively disables entity comparison */
         COMPARE_NONE

         ;

        // @formatter:on

        }   // end enum CompareOn


    /*
     * static data
     */


    /**
     * controls the selection of attributes to use in {@code Card} comparisons
     */
    private static CompareOn compareOnAttributes = CompareOn.COMPARE_SUIT_AND_RANK ;


    /*
     * data fields
     */


    /** The card's suit */
    public final Suit suit ;

    /** The card's rank within its suit */
    public final Rank rank ;


    /*
     * constructors
     */


    /**
     * Initialize a card with no suit (e.g., a joker)
     *
     * @param theRank
     *     this card's rank
     */
    public Card( final Rank theRank )
        {

        this( theRank, Suit.NA ) ;

        }   // end 1-arg constructor


    /**
     * Initialize a card with a specified suit and rank
     *
     * @param theRank
     *     this card's rank
     * @param theSuit
     *     this card's suit
     */
    public Card( final Rank theRank,
                 final Suit theSuit )
        {

        Objects.requireNonNull( theSuit, "theSuit" ) ;
        Objects.requireNonNull( theRank, "theRank" ) ;

        this.suit = theSuit ;
        this.rank = theRank ;

        super.setFaceUpText( String.format( "%s%s", this.rank, this.suit ) ) ;

        }   // end 2-arg constructor


    /**
     * create a temporary clone of a card, typically for searching
     *
     * @param sourceCard
     *     the card to copy
     */
    public Card( final Card sourceCard )
        {

        this( sourceCard, TEMPORARY ) ;

        }   // end 1-arg 'cloning' constructor


    /**
     * create a clone of a card, typically for searching
     *
     * @param sourceCard
     *     the card to copy
     * @param cardPersistence
     *     the persistence for the card
     */
    public Card( final Card sourceCard,
                 final Persistence cardPersistence )
        {

        super( sourceCard, cardPersistence ) ;

        // assertion: we have a source card

        this.suit = sourceCard.suit ;
        this.rank = sourceCard.rank ;

        super.setOrientation( sourceCard.orientation ) ;

        }   // end 2-arg 'cloning' constructor


    /*
     * getters and setters
     */
    // none - unnecessary


    /*
     * methods to affect card comparison behavior
     */


    /**
     * Retrieves the current behavior of {@code Card} comparisons
     *
     * @return the current setting
     */
    public static CompareOn getCompareOnAttributes()
        {

        return Card.compareOnAttributes ;

        }  // end getCompareOnAttributes()


    /**
     * Sets the behavior of {@code Card} comparisons
     *
     * @param newCompareOnAttributes
     *     the new evaluation behavior wrt card comparisons
     *
     * @return the previous state
     */
    public static CompareOn setCompareOnAttributes( final CompareOn newCompareOnAttributes )
        {

        Objects.requireNonNull( newCompareOnAttributes, "newCompareOnAttributes" ) ;

        final CompareOn wasCompareOnAttributes = Card.compareOnAttributes ;

        Card.compareOnAttributes = newCompareOnAttributes ;

        return wasCompareOnAttributes ;

        }  // end setCompareOnAttributes()


    /*
     * overridden superclass methods - make it easy for these methods to be
     * fluent
     */


    /*
     * methods to affect face up/down state and display of an instance
     */


    @Override
    public Card setFaceDown()
        {

        setOrientation( FACE_DOWN ) ;

        return this ;

        }   // end setFaceDown()


    @Override
    public Card setFaceUp()
        {

        setOrientation( FACE_UP ) ;

        return this ;

        }   // end setFaceUp()


    @Override
    public Card flip()
        {

        return (Card) super.flip() ;

        }   // end flip()


    @Override
    public Card hide()
        {

        return (Card) super.hide() ;

        }   // end hide()


    @Override
    public Card reveal()
        {

        return (Card) super.reveal() ;

        }   // end reveal()


    /*
     * general methods
     */


    @Override
    public int compareTo( final CardBase otherCard )
        {

        // make sure comparisons are permitted
        if ( compareOnNone() )
            {
            throw new UnsupportedOperationException( "comparisons are disabled" ) ;
            }

        // if other card is a standard playing card
        // compare rank then suit as enabled/disabled

        if ( otherCard instanceof final Card otherStandardCard )
            {
            // other card is one of ours

            int cardComparison = 0 ;

            // assertion: comparison value will be (re)set by either or both of
            // the tests

            // check rank
            if ( compareOnRank() )
                {
                cardComparison = this.rank.getAltOrder() - otherStandardCard.rank.getAltOrder() ;
                }

            // check suit, if necessary
            if ( ( cardComparison == 0 ) && compareOnSuit() )
                {
                cardComparison = this.suit.getAltPriority() - otherStandardCard.suit.getAltPriority() ;
                }

            return cardComparison ;
            }

        // other card is not one of ours or is null

        throw new IllegalArgumentException( String.format( "other card must be a %s but is a %s",
                                                           this.getClass().getSimpleName(),
                                                           otherCard == null
                                                                   ? null
                                                                   : otherCard.getClass()
                                                                              .getSimpleName() ) ) ;

        }   // end compareTo()


    @Override
    public boolean equals( final Object otherObject )
        {

        // make sure comparisons are permitted
        if ( Card.compareOnNone() )
            {
            throw new UnsupportedOperationException( "comparisons are disabled" ) ;
            }

        // same object?
        if ( this == otherObject )
            {
            return true ;
            }

        // another standard card? false if otherObject is null
        if ( otherObject instanceof final Card otherCard )
            {
            return compareTo( otherCard ) == 0 ;
            }

        // not one of ours so can't match
        return false ;

        }   // end equals()


    @Override
    public int hashCode()
        {

        // make sure comparisons are permitted
        if ( Card.compareOnNone() )
            {
            throw new UnsupportedOperationException( "comparisons are disabled" ) ;
            }

        // assertion: rank and/or suit will be included

        return Objects.hash( compareOnRank()
                ? this.rank
                : 0,
                             compareOnSuit()
                                     ? this.suit
                                     : 0 ) ;

        }   // end hashCode()


    /**
     * {@inheritDoc}
     * <p>
     * prevents comparison if disabled
     */
    @Override
    public boolean matches( final CardBase otherBaseCard )
        {

        // make sure comparisons are permitted
        if ( compareOnNone() )
            {
            throw new UnsupportedOperationException( "comparisons are disabled" ) ;
            }

        // same object?
        if ( this == otherBaseCard )
            {
            return true ;
            }

        // another standard card? false if otherBaseCard is null
        if ( otherBaseCard instanceof final Card otherCard )
            {
            // delegate to the component version
            return this.matches( otherCard.rank, otherCard.suit ) ;
            }

        // not one of ours so can't match
        return false ;

        }   // end by-card matches()


    /**
     * determine if this card matches the specified components
     *
     * @param matchToRank
     *     the rank to compare against ours if specified (non-{@code null})
     * @param matchToSuit
     *     the suit to compare against ours if specified (non-{@code null})
     *
     * @return {@code true} if both
     */
    public boolean matches( final Rank matchToRank,
                            final Suit matchToSuit )
        {

        // make sure comparisons are permitted
        if ( compareOnNone() )
            {
            throw new UnsupportedOperationException( "comparisons are disabled" ) ;
            }

        // match if rank or suit are the same
        return ( compareOnRank() && ( this.rank == matchToRank ) )
               || ( compareOnSuit() && ( this.suit == matchToSuit ) ) ;

        }   // end by-components matches()


    /**
     * create a new card resembling the provided card
     *
     * @param sourceCard
     *     card to mimic
     *
     * @return a new card resembling the source card
     */
    public static Card newCardLike( final Card sourceCard )
        {

        Objects.requireNonNull( sourceCard, "sourceCard" ) ;

        return newCardLike( sourceCard.rank, sourceCard.suit ) ;

        }   // end by-card newCardLike()


    /**
     * create a new card for the specified components
     *
     * @param newRank
     *     rank for the new card
     * @param newSuit
     *     suit for the new card
     *
     * @return a new card of the type compatible with the kind of the value
     */
    public static Card newCardLike( final Rank newRank,
                                    final Suit newSuit )
        {

        Objects.requireNonNull( newSuit, "newSuit" ) ;
        Objects.requireNonNull( newRank, "newRank" ) ;

        return new Card( newRank, newSuit ) ;

        }   // end by-components newCardLike()


    /*
     * private utility methods
     */


    /**
     * convenience method to determine if comparisons of cards is disabled
     *
     * @return {@code true} if comparisons should not be done, {@code false}
     *     otherwise
     */
    private static boolean compareOnNone()
        {

        return Card.compareOnAttributes == CompareOn.COMPARE_NONE ;

        }   // end compareOnNone()


    /**
     * convenience method to determine if rank should be considered when
     * comparing two cards
     *
     * @return {@code true} if comparisons should consider rank, {@code false}
     *     otherwise
     */
    private static boolean compareOnRank()
        {

        return ( Card.compareOnAttributes == CompareOn.COMPARE_RANK_ONLY )
               || ( Card.compareOnAttributes == CompareOn.COMPARE_SUIT_AND_RANK ) ;

        }   // end compareOnRank()


    /**
     * convenience method to determine if suit should be considered when
     * comparing two cards
     *
     * @return {@code true} if comparisons should consider suit, {@code false}
     *     otherwise
     */
    private static boolean compareOnSuit()
        {

        return ( Card.compareOnAttributes == CompareOn.COMPARE_SUIT_ONLY )
               || ( Card.compareOnAttributes == CompareOn.COMPARE_SUIT_AND_RANK ) ;

        }   // end compareOnSuit()


    /*
     * inner classes
     */
    // none


    /*
     * for testing/debugging
     */


    /**
     * Sample demo program
     *
     * @param args
     *     -unused-
     */
    public static void main( final String[] args )
        {

        final Suit[] suits = Suit.values() ;
        final Rank[] ranks = Rank.values() ;

        final List<Card> cards = new ArrayList<>( suits.length * ranks.length ) ;

        System.out.printf( "Key: ranks are Ace (1) .. King (13)%n%n" ) ;

        // generate a deck of cards
        System.out.printf( "New cards:%n" ) ;

        // make them permanent
        setDefaultPersistence( Persistence.PERMANENT ) ;

        for ( final Suit suit : suits )
            {

            // skip placeholder suit
            if ( suit == NA )
                {
                continue ;
                }

            for ( final Rank rank : ranks )
                {

                // skip non-playing card(s) - Joker
                if ( rank == JOKER )
                    {
                    continue ;
                    }

                // build a card
                final Card newCard = new Card( rank, suit ) ;
                System.out.printf( " %s", newCard ) ;

                // keep track of it
                cards.add( newCard ) ;
                }

            }

        // reset card permanence
        resetDefaultPersistence() ;

        // turn top card over
        cards.getFirst().flip() ;

        // display all the cards
        System.out.printf( "%n%nAll cards:%n%s%n%n", cards.toString() ) ;

        // turn all cards face up
        for ( final Card aCard : cards )
            {
            aCard.reveal() ;
            }

        // display all the cards
        System.out.printf( "%n%nAll cards:%n%s%n%n", cards.toString() ) ;

        // shuffled
        Collections.shuffle( cards ) ;
        System.out.printf( "%nShuffled:%n%s%n%n", cards.toString() ) ;

        // sorted
        Collections.sort( cards ) ;
        System.out.printf( "%nSorted (rank and suit):%n%s%n%n", cards.toString() ) ;

        // sort only on rank
        setCompareOnAttributes( CompareOn.COMPARE_RANK_ONLY ) ;

        // shuffled
        Collections.shuffle( cards ) ;
        System.out.printf( "%nShuffled:%n%s%n%n", cards.toString() ) ;

        // sorted
        Collections.sort( cards ) ;
        System.out.printf( "%nSorted (rank only):%n%s%n%n", cards.toString() ) ;

        // sorted
        Collections.sort( cards ) ;
        System.out.printf( "%nSorted (rank only):%n%s%n%n", cards.toString() ) ;

        // sort on rank and suit
        setCompareOnAttributes( CompareOn.COMPARE_SUIT_AND_RANK ) ;

        // sorted
        Collections.sort( cards ) ;
        System.out.printf( "%nSorted (rank and suit):%n%s%n%n", cards.toString() ) ;


        // compare some cards against each other
        Card card1 = cards.get( 2 ) ;
        Card card2 = cards.get( 3 ) ;
        System.out.printf( "%s.compareTo(%s) = %+,d (rank and suit)%n",
                           card1,
                           card2,
                           card1.compareTo( card2 ) ) ;
        System.out.printf( "%s.compareTo(%s) = %+,d (rank and suit)%n",
                           card2,
                           card1,
                           card2.compareTo( card1 ) ) ;

        card1 = cards.get( 15 ) ;
        card2 = cards.get( 43 ) ;
        System.out.printf( "%s.compareTo(%s) = %+,d (rank and suit)%n",
                           card1,
                           card2,
                           card1.compareTo( card2 ) ) ;


        card2 = cards.get( 4 ) ;
        System.out.printf( "%s.compareTo(%s) = %+,d (rank and suit)%n",
                           card1,
                           card2,
                           card1.compareTo( card2 ) ) ;

        card2 = cards.get( 20 ) ;
        System.out.printf( "%s.compareTo(%s) = %+,d (rank and suit)%n",
                           card1,
                           card2,
                           card1.compareTo( card2 ) ) ;


        System.out.printf( "%n" ) ;
        card1 = cards.get( 2 ) ;
        card2 = cards.get( 3 ) ;
        System.out.printf( "%s.equals(%s) = %b (rank and suit)%n", card1, card2, card1.equals( card2 ) ) ;
        System.out.printf( "%s.equals(%s) = %b (rank and suit)%n", card1, card1, card1.equals( card1 ) ) ;
        System.out.printf( "%s.equals(%s) = %b (rank and suit)%n", card2, card1, card2.equals( card1 ) ) ;
        System.out.printf( "%s == %s = %b (rank and suit)%n", card1, card2, card1 == card2 ) ;


        System.out.printf( "%ncreating temporary cards%n" ) ;
        CardBase.setDefaultOrientation( FACE_UP ) ;
        card1 = new Card( FOUR, DIAMONDS ) ;
        card2 = new Card( FOUR, HEARTS ) ;
        System.out.printf( "%s.equals(%s) = %b (rank and suit)%n", card1, card2, card1.equals( card2 ) ) ;


        System.out.printf( "%n" ) ;
        System.out.printf( "%s.matches(%s) = %b (rank and suit)%n", card1, card1, card1.matches( card1 ) ) ;
        System.out.printf( "%s.matches(%s) = %b (rank and suit)%n", card1, card2, card1.matches( card2 ) ) ;
        System.out.printf( "%s == %s = %b (rank and suit)%n", card1, card2, card1 == card2 ) ;

        CardBase.setDefaultOrientation( FACE_UP ) ;
        card1 = new Card( FOUR, DIAMONDS ) ;
        card2 = new Card( FOUR, HEARTS ) ;
        System.out.printf( "%s.matches(%s) = %b (rank and suit)%n", card1, card2, card1.matches( card2 ) ) ;


        // repeat comparisons without considering suit
        setCompareOnAttributes( CompareOn.COMPARE_RANK_ONLY ) ;

        System.out.printf( "%n" ) ;

        // compare some cards against each other
        card1 = cards.get( 15 ) ;
        card2 = cards.get( 43 ) ;
        System.out.printf( "%s.compareTo(%s) = %+,d (rank only)%n", card1, card2, card1.compareTo( card2 ) ) ;

        card2 = cards.get( 4 ) ;
        System.out.printf( "%s.compareTo(%s) = %+,d (rank only)%n", card1, card2, card1.compareTo( card2 ) ) ;

        card2 = cards.get( 20 ) ;
        System.out.printf( "%s.compareTo(%s) = %+,d (rank only)%n", card1, card2, card1.compareTo( card2 ) ) ;


        System.out.printf( "%n" ) ;
        System.out.printf( "%s.equals(%s) = %b (rank only)%n", card1, card1, card1.equals( card1 ) ) ;
        System.out.printf( "%s.equals(%s) = %b (rank only)%n", card1, card2, card1.equals( card2 ) ) ;


        setCompareOnAttributes( CompareOn.COMPARE_SUIT_AND_RANK ) ;

        CardBase.setDefaultOrientation( FACE_UP ) ;
        card1 = new Card( FOUR, DIAMONDS ) ;
        card2 = new Card( FOUR, HEARTS ) ;
        System.out.printf( "%s.equals(%s) = %b (rank and suit)%n", card1, card2, card1.equals( card2 ) ) ;


        CardBase.setDefaultOrientation( FACE_UP ) ;
        card1 = new Card( QUEEN, DIAMONDS ) ;
        card2 = new Card( QUEEN, HEARTS ) ;
        System.out.printf( "%s.equals(%s) = %b (rank and suit)%n", card1, card2, card1.equals( card2 ) ) ;
        System.out.printf( "%s.equals(%s) = %b (rank and suit)%n", card2, card1, card2.equals( card1 ) ) ;


        CardBase.setDefaultOrientation( FACE_UP ) ;
        card1 = new Card( KING, DIAMONDS ) ;
        card2 = new Card( QUEEN, SPADES ) ;
        System.out.printf( "%s.equals(%s) = %b (rank and suit)%n", card1, card2, card1.equals( card2 ) ) ;
        System.out.printf( "%s.equals(%s) = %b (rank and suit)%n", card2, card1, card2.equals( card1 ) ) ;


        setCompareOnAttributes( CompareOn.COMPARE_RANK_ONLY ) ;

        System.out.printf( "%n" ) ;
        System.out.printf( "%s.matches(%s) = %b (rank only)%n", card1, card1, card1.matches( card1 ) ) ;
        System.out.printf( "%s.matches(%s) = %b (rank only)%n", card1, card2, card1.matches( card2 ) ) ;

        CardBase.setDefaultOrientation( FACE_UP ) ;
        card1 = new Card( FOUR, DIAMONDS ) ;
        card2 = new Card( FOUR, HEARTS ) ;
        System.out.printf( "%s.matches(%s) = %b (rank only)%n", card1, card2, card1.matches( card2 ) ) ;

        card1 = new Card( Rank.SIX, Suit.CLUBS ) ;
        card2 = new Card( QUEEN, SPADES ) ;
        System.out.printf( "%s.matches(%s) = %b%n", card1, card2, card1.matches( card2 ) ) ;


        System.out.printf( "%ncreating template cards%n" ) ;
        CardBase.setDefaultPersistence( TEMPLATE ) ;
        CardBase.setDefaultOrientation( FACE_UP ) ;
        card1 = new Card( FOUR, DIAMONDS ) ;
        card2 = new Card( card2 ) ;
        System.out.printf( "%s.equals(%s) = %b (rank and suit)%n", card1, card2, card1.equals( card2 ) ) ;


        System.out.printf( "%n" ) ;
        System.out.printf( "%s.matches(%s) = %b (rank and suit)%n", card1, card1, card1.matches( card1 ) ) ;
        System.out.printf( "%s.matches(%s) = %b (rank and suit)%n", card1, card2, card1.matches( card2 ) ) ;
        System.out.printf( "%s == %s = %b (rank and suit)%n", card1, card2, card1 == card2 ) ;


        System.out.printf( "%ncreating temporary cards%n" ) ;
        CardBase.resetDefaultPersistence() ;
        CardBase.setDefaultOrientation( FACE_UP ) ;
        card1 = new Card( FOUR, DIAMONDS ) ;
        card2 = new Card( card2 ) ;
        System.out.printf( "%s.equals(%s) = %b (rank and suit)%n", card1, card2, card1.equals( card2 ) ) ;


        System.out.printf( "%n" ) ;
        System.out.printf( "%s.matches(%s) = %b (rank and suit)%n", card1, card1, card1.matches( card1 ) ) ;
        System.out.printf( "%s.matches(%s) = %b (rank and suit)%n", card1, card2, card1.matches( card2 ) ) ;
        System.out.printf( "%s == %s = %b (rank and suit)%n", card1, card2, card1 == card2 ) ;

        }   // end main()

    }   // end class Card