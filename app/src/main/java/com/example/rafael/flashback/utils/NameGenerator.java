package com.example.rafael.flashback.utils;

/**
 * Created by Dianru Liu on 2018/3/3.
 */

public class NameGenerator {
    private String realname;
    private String proxyname;
    private String [] animals = {"alligator","anteater","armadillo","auroch","axolotl","badger","bat",
            "beaver","buffalo","camel","chameleon","cheetah","chipmunk","chinchilla","chupacabra",
            "cormorant","coyote","crow","dingo","dinosaur","dolphin","duck","elephant",
            "ferret","fox","frog","giraffe","gopher","grizzly","hedgehog","hippo","hyena",
            "jackal","ibex","ifrit","iguana","koala","kraken","lemur","leopard","liger","llama",
            "manatee","mink","monkey","narwhal","nyan cat","orangutan","otter","panda","penguin",
            "platypus","python","pumpkin","quagga","rabbit","raccoon","rhino","sheep","shrew",
            "skunk","slow loris","squirrel","turtle","walrus","wolf","wolverine","wombat"};

    public NameGenerator(){
        realname = "default";
        proxyname = "default";
    }
    public String proxy(String username){
        realname = username;
        int hash = 7;
        for(int i = 0; i<realname.length();i++){
            hash = hash*31 + realname.charAt(i);
        }
        if(hash<0)
        {
            hash = -1*hash;
        }
        int temp = hash/animals.length/10000;
        while(temp > 1000000)
        {
            temp = temp/10000;
        }
        proxyname = animals[(hash % animals.length)] + temp;
        return proxyname;
    }
    public int getSize(){
        return animals.length;
    }
}
