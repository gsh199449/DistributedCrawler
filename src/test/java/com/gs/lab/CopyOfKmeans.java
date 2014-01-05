package com.gs.lab;

public class CopyOfKmeans {
    
    private static int K = 2; // 类数（簇） 此程序为2 
    private static int TOTAL = 20; // 点个数 此程序为20
    private int test = 0;
    private Point[] unknown = new Point[TOTAL]; // 点数组
    private int[] type = new int[TOTAL]; // 每个点暂时的类（簇）
    private Point[] z = new Point[K];// 保存新的聚类中心
    private Point[] z0 = new Point[K]; // 保存上一次的聚类中心
    private Point sum = new Point(0.0,0.0);
    private int I = 0; // 迭代次数
   
    /** Creates a new instance of Kmeans */
    public CopyOfKmeans() {
        /** 进行聚类运算的20个点 */
        unknown[0] = new Point(0,0);
        unknown[1] = new Point(1,0);
        unknown[2] = new Point(0,1);
        unknown[3] = new Point(1,1);
        unknown[4] = new Point(2,1);
        unknown[5] = new Point(1,2);
        unknown[6] = new Point(2,2);
        unknown[7] = new Point(3,2);
        unknown[8] = new Point(6,6);
        unknown[9] = new Point(7,6);
        unknown[10] = new Point(8,6);
        unknown[11] = new Point(6,7);
        unknown[12] = new Point(7,7);
        unknown[13] = new Point(8,7);
        unknown[14] = new Point(9,7);
        unknown[15] = new Point(7,8);
        unknown[16] = new Point(8,8);
        unknown[17] = new Point(9,8);
        unknown[18] = new Point(8,9);
        unknown[19] = new Point(9,9);
        
        for(int i = 0;i < TOTAL; i++){
            type[i] = 0;
        }
        for(int i = 0; i < K; i++){
            z[i] = unknown[i]; // 伪随机选取
            z0[i] = new Point(0.0,0.0);
        }
    }
    
    /** 计算新的聚类中心 */
    public Point newCenter(int m){
        int n = 0;
        for(int i = 0;i < TOTAL; i++){
            if(type[i] == m){
                sum.setX(sum.getX() + unknown[i].getX());
                sum.setY(sum.getY() + unknown[i].getY());
                n += 1;
            }
        }
        sum.setX(sum.getX() / n);
        sum.setY(sum.getY() / n);
        return sum;
    }
    
    /** 比较两个聚类中心是否相等 */
    public boolean isEqual(Point p1,Point p2){
        if(p1.getX() * 10 == p2.getX() * 10 && p1.getY() * 10 == p2.getY() * 10)
            return true;
        else
            return false;
    }
    
    /** 计算两点之间的欧式距离 */
    public static double distance(Point p1,Point p2){
        return (p1.getX() - p2.getX()) * (p1.getX() - p2.getX()) + 
                (p1.getY() - p2.getY()) * (p1.getY() - p2.getY());
    }
    
    /** 进行迭代，对TOTAL个样本根据聚类中心进行分类 */
    public void order(){
        int temp = 0; // 记录unknown[i]暂时在哪个类中
        for(int i = 0; i < TOTAL;i ++){
            for(int j = 0; j < 2;j++){
                if(distance(unknown[i],z[temp]) > distance(unknown[i],z[j]))
                    temp = j;
            }
            type[i] = temp;
            System.out.println("经比较后，" + unknown[i].toString() + "被归为" + temp +"类" );
        }
    }
    
    public void main(){
        System.out.println("共有如下个未知样本:");
        for(int i = 0; i < TOTAL;i++){
            System.out.println(unknown[i]);
            //System.out.println("初始时，第" + i + "类中心:" + z[i].toString());
        }
        for(int i = 0; i < K;i++)
            System.out.println("初始时，第" + i + "类中心:" + z[i].toString());
        while(test != K){
            order();
            for(int i = 0; i < K;i ++){
                z[i] = newCenter(i);
                System.out.println("第" + i + " 类新中心:" + z[i].toString());
                if(isEqual(z[i],z0[i]))
                    test += 1;
                else
                    z0[i] = z[i];
            }
            I += 1;
            System.out.println("已完成第" + I + "次迭代");
            System.out.println("分类后有:");
            for(int j = 0;j < K;j++){
                System.out.println("第" + j + "类分类有: ");
                for(int i = 0;i < TOTAL;i++){
                    if(type[i] == j)
                        System.out.println(unknown[i].toString());
                }
            }
        }
    }
    
    public static void main(String[] args){
        new CopyOfKmeans().main();
    }
}

/*
 * Point.java
 *
 * Created on 2007年1月22日, 下午12:50
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

class Point {
    
    private double x = 0;
    private double y = 0;
    
    /** Creates a new instance of Point */
    public Point(double x,double y) {
        this.setX(x);
        this.setY(y);
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
    
    public String toString(){
        return "[" + x + "," + y + "]";
    }
    
}