package net.sorenon.minexraft.client.accessor;

public interface MouseExt {

    void cursorPos(double x, double y);

    void mouseButton(int button, int action, int mods);

    void mouseScroll(double horizontalDistance, double verticalDistance);
}
