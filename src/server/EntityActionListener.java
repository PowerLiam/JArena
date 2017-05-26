package server;

public interface EntityActionListener {

    void die(Entity toMourn);

    void shotBullet(Bullet b);

}
