package club.bluegem.pokerhud

import org.junit.Test

import org.junit.Assert.*

/**
 * Created by 01020409 on 2017/07/31.
 */
class PlayerTest {
    val player:Player =  Player("1")
    @Test
    fun changeStatus() {
        assertFalse(player.playerStatus)
        player.changeStatus()
        assertTrue(player.playerStatus)
        player.changeStatus()
        assertFalse(player.playerStatus)
    }

    @Test
    fun addHand() {
        assertEquals("0",player.playedHandCount)
        player.addHand()
        assertEquals("1",player.playedHandCount)

    }

    @Test
    fun addCalledHand() {
        assertEquals(0,player.calledHandCount)
        player.addCalledHand()
        assertEquals(1,player.calledHandCount)

    }

    @Test
    fun addRaisedHand() {
        assertEquals(0,player.raisedHandCount)
        player.addRaisedHand()
        assertEquals(1,player.raisedHandCount)

    }

    @Test
    fun addDealerButton() {
        assertEquals(0,player.dealerButtonCount)
        player.addDealerButton()
        assertEquals(1,player.dealerButtonCount)

    }

    @Test
    fun addDealerButtonRaised() {
        assertEquals(0,player.dealerButtonRaisedCount)
        player.addDealerButtonRaised()
        assertEquals(1,player.dealerButtonRaisedCount)
    }

    @Test
    fun calc() {

    }

    @Test
    fun resetAll() {
        player.changeStatus()
        player.addHand()
        assertEquals("1",player.playedHandCount)
        assertTrue(player.playerStatus)
        player.resetAll()
        assertEquals("0",player.playedHandCount)
        assertFalse(player.playerStatus)

    }

}