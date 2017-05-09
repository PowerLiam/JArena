package Server;

import Global.Position;

public interface EntityActionListener{

    void die(Entity toMourn);
    void move(Entity toMove, Position newPosition);

}
