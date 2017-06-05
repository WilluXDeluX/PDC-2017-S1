/*
this class is the monster class it extentds the unit class
it sets up the monster
@author William
 */
package Game;

/**
 *
 * @author William
 */
public class Monster extends Unit{
    
    public Monster(String name, int[] stats,int type, int damageClass)
    {
        super(name, stats);
        setDamageClass(damageClass);
        setType(type);
    }
    
    
}
