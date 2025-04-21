package com.phasmidsoftware.dsaipg.projects.mcts.connectfour;

import com.phasmidsoftware.dsaipg.projects.mcts.core.Node;
import com.phasmidsoftware.dsaipg.projects.mcts.core.State;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.util.Optional;
import java.util.Collection;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class ConnectFourNodeTest {

    @Mock
    private State<ConnectFourGame> state;

    private ConnectFourNode connectFourNode;

    @Before
    public void setup() {
        when(state.isTerminal()).thenReturn(false);
        when(state.player()).thenReturn(ConnectFourGame.PLAYER_1);
        connectFourNode = new ConnectFourNode(state);
    }

    @Test
    public void shouldIsLeaf() {
        assertFalse(connectFourNode.isLeaf());
    }

    @Test
    public void shouldState() {
        assertEquals(state, connectFourNode.state());
    }

    @Test
    public void shouldWhite() {
        // Assuming opponent of PLAYER_1 is PLAYER_2
        when(state.player()).thenReturn(ConnectFourGame.PLAYER_2);
        ConnectFourNode node = new ConnectFourNode(state);
        assertTrue(node.white());
    }

    @Test
    public void shouldChildrenInitiallyEmpty() {
        assertTrue(connectFourNode.children().isEmpty());
    }

    @Test
    public void shouldAddChild() {
        State<ConnectFourGame> childState = mock(State.class);
        ConnectFourGame mockGame = mock(ConnectFourGame.class);

        when(childState.isTerminal()).thenReturn(true);
        when(childState.player()).thenReturn(ConnectFourGame.PLAYER_1);
        when(childState.game()).thenReturn(mockGame);
        when(childState.winner()).thenReturn(Optional.of(ConnectFourGame.PLAYER_1));

        connectFourNode.addChild(childState);

        Collection<Node<ConnectFourGame>> children = connectFourNode.children();
        assertEquals(1, children.size());
    }

    @Test
    public void shouldBackPropagateFromLeaf() {
        State<ConnectFourGame> leafState = mock(State.class);
        when(leafState.isTerminal()).thenReturn(true);
        when(leafState.player()).thenReturn(ConnectFourGame.PLAYER_2);
        when(leafState.winner()).thenReturn(Optional.of(ConnectFourGame.PLAYER_1));
        when(leafState.game()).thenReturn(mock(ConnectFourGame.class));

        ConnectFourNode leafNode = new ConnectFourNode(leafState);
        leafNode.backPropagate();

        assertEquals(1, leafNode.playouts());
    }

    @Test
    public void shouldWinsAndPlayoutsInitiallyBeZero() {
        assertEquals(0, connectFourNode.wins());
        assertEquals(0, connectFourNode.playouts());
    }

    @Test
    public void shouldToStringNotNull() {
        assertNotNull(connectFourNode.toString());
    }

    @Test
    public void shouldEqualsAndHashCode() {
        ConnectFourNode another = new ConnectFourNode(state);
        assertEquals(connectFourNode, another);
        assertEquals(connectFourNode.hashCode(), another.hashCode());
    }
}
