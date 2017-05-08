package Global;

public interface EntityActionListener{

    void die(Entity toMourn);
    void move(Entity toMove, Position newPosition);

}
