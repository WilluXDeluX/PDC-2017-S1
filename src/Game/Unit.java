/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game;

/**
 *this class is the unit class it is extened to monster and player 
 * it gets the monster and player statistics and sets them up 
 * @author William & Callum
 */
public class Unit {
    
    private String name;
    private int damageClass;
    private int hp;
    private int attack;
    private int armor;
    private int strength;
    private int dexterity;
    private int wisdom;
    private int constitution;
    private int intelect;
    private int[] stats;
    private int type;
    
    public Unit(String name, int[] stats)
    {
        this.name = name;
        this.stats = stats;
        hp = stats[0];
        attack = stats[1];
        armor = stats[2];
        strength = stats[3];
        dexterity = stats[4];
        wisdom = stats[5];
        constitution = stats[6];
        intelect = stats[7];
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getArmor() {
        return armor;
    }

    public void setArmor(int armor) {
        this.armor = armor;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public int getDexterity() {
        return dexterity;
    }

    public void setDexterity(int dexterity) {
        this.dexterity = dexterity;
    }

    public int getWisdom() {
        return wisdom;
    }

    public void setWisdom(int wisdom) {
        this.wisdom = wisdom;
    }

    public int getConstitution() {
        return constitution;
    }

    public void setConstitution(int constitution) {
        this.constitution = constitution;
    }
    
    public int getIntelect() {
        return intelect;
    }

    public void setIntelect(int intelect) {
        this.intelect = intelect;
    }

    public int getDamageClass() {
        return damageClass;
    }

    public void setDamageClass(int damageClass) {
        this.damageClass = damageClass;
    }
    
    public int getType() {
        return type;
    }
    
    public void setType(int type) {
        this.type = type;
    }

    public int[] getStats() {
        return stats;
    }

}
