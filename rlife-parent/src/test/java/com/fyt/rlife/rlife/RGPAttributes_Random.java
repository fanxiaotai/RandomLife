package com.fyt.rlife.rlife;

import java.util.*;

/**
 * @Author: fanyitai
 * @Date: 2020/1/5 11:12
 * @Version 1.0
 */
public class RGPAttributes_Random {
    public static Role randomAttributes(){
        Role role = roleRandom();
        return role;
    }

    /**
     * 4枚骰子，去除最小数之和
     * @return
     */
    public static int dicing(){
        int[] dic = new int[4];
        Random random = new Random();
        for (int i =0;i<4;i++){
            int j = random.nextInt(6);
            dic[i] = j+1;
        }
        //冒泡排序，把最小值放到最后面
        for (int i = 0;i<dic.length-1;i++){
            int a = 0;
            if (dic[i]<dic[i+1]){
                a = dic[1];
                dic[i] = dic[i+1];
                dic[i+1]=a;
            }
        }
        return dic[0]+dic[1]+dic[2];

    }

    public static Role roleRandom(){
        List<Integer> list = new ArrayList<>();
        Role role = new Role();
        int dicing = dicing();
        list.add(dicing);
        role.set力量(dicing);
        dicing = dicing();
        list.add(dicing);
        role.set敏捷(dicing);
        dicing = dicing();
        list.add(dicing);
        role.set体格(dicing);
        dicing = dicing();
        list.add(dicing);
        role.set智力(dicing);
        dicing = dicing();
        list.add(dicing);
        role.set学识(dicing);
        dicing = dicing();
        list.add(dicing);
        role.set魅力(dicing);
        int a = 0;
        int b = 0;
        for (Integer integer : list) {
            if (integer>=15){
                b++;
            }
            a+=integer;
        }
        if (a<75){
            return roleRandom();
        }
        if (b<2){
            return roleRandom();
        }
        System.out.println("属性总和="+a);
        for (int i = 0;i<list.size();i++){
            System.out.println("第"+i+1+"个属性值是"+list.get(i));
        }
        return role;
    }

    public static void main(String[] args) {
        for (int i = 0;i<10;i++){
            Role role = randomAttributes();
            System.out.println(role);
        }
    }
}

class Role{
    private Integer 力量;
    private Integer 敏捷;
    private Integer 体格;
    private Integer 智力;
    private Integer 学识;
    private Integer 魅力;

    public Integer get力量() {
        return 力量;
    }

    public void set力量(Integer 力量) {
        this.力量 = 力量;
    }

    public Integer get敏捷() {
        return 敏捷;
    }

    public void set敏捷(Integer 敏捷) {
        this.敏捷 = 敏捷;
    }

    public Integer get体格() {
        return 体格;
    }

    public void set体格(Integer 体格) {
        this.体格 = 体格;
    }

    public Integer get智力() {
        return 智力;
    }

    public void set智力(Integer 智力) {
        this.智力 = 智力;
    }

    public Integer get学识() {
        return 学识;
    }

    public void set学识(Integer 学识) {
        this.学识 = 学识;
    }

    public Integer get魅力() {
        return 魅力;
    }

    public void set魅力(Integer 魅力) {
        this.魅力 = 魅力;
    }

    @Override
    public String toString() {
        return "Role{" +
                "力量=" + 力量 +
                ", 敏捷=" + 敏捷 +
                ", 体格=" + 体格 +
                ", 智力=" + 智力 +
                ", 学识=" + 学识 +
                ", 魅力=" + 魅力 +
                '}';
    }
}
