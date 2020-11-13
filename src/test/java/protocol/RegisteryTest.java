package protocol;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Author: Ashish Gogna
 */
public class RegisteryTest {

    @Test
    public void addNode() {
        initNode();
        String newNodeEp = "XX.XX.XX.XX";
        Node.getInstance().getRegistery().addNode(newNodeEp);
        boolean added = false;
        try { added = DataStore.readNodes().contains(newNodeEp); } catch (Exception ignored) { }
        assertTrue(added);
    }

    @Test
    public void addAuth() {
        initNode();
        String auth = "ZZZ";
        Node.getInstance().getRegistery().addAuth(auth);
        boolean added = false;
        try { added = DataStore.readAuths().contains(auth); } catch (Exception ignored) { }
        assertTrue(added);
    }

    @Test
    public void addLwc() {
        initNode();
        String lwc = "YYY";
        Node.getInstance().getRegistery().addLwc(lwc);
        boolean added = false;
        try { added = DataStore.readLWCs().contains(lwc); } catch (Exception ignored) { }
        assertTrue(added);
    }

    @Test
    public void remNode() {
        initNode();
        String nodeEp = "XX.XX.XX.XXX";
        Node.getInstance().getRegistery().remNode(nodeEp);
        boolean removed = false;
        try { removed = !DataStore.readNodes().contains(nodeEp); } catch (Exception ignored) { ignored.printStackTrace(); }
        assertTrue(removed);
    }

    @Test
    public void remAuth() {
        initNode();
        String auth = "ZZZ";
        Node.getInstance().getRegistery().remAuth(auth);
        boolean removed = false;
        try { removed = !DataStore.readAuths().contains(auth); } catch (Exception ignored) { ignored.printStackTrace(); }
        assertTrue(removed);
    }

    @Test
    public void remLwc() {
        initNode();
        String lwc = "YYY";
        Node.getInstance().getRegistery().remLwc(lwc);
        boolean removed = false;
        try { removed = !DataStore.readLWCs().contains(lwc); } catch (Exception ignored) { ignored.printStackTrace(); }
        assertTrue(removed);
    }

    private void initNode() {
        try { Node.initialize(""); } catch (Exception ignored) { }
    }
}