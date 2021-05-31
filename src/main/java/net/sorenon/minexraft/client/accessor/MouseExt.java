package net.sorenon.minexraft.client.accessor;

public interface MouseExt {

    void cursorPos(int x, int y);

    void mouseButton(int button, int action, int mods);
}
