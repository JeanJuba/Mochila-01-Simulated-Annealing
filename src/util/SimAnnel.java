/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.util.Arrays;
import java.util.Random;

/**
 *
 * @author Usuario
 */
public class SimAnnel {

    private double temperaturaMinima = 0.0001;
    private double temperaturaAtual = 800;
    private double fatorAlpha = 0.9999;
    private int pesoMax = 400;
    
    private String[] itemNames;
    private double[] pesos;
    private double[] beneficios;

    private int saMax = 50;
    private int itAtual;

    private int[] solucaoOriginal;
    //private double beneficioAtual;

    private int[] melhorSolucao;
    private double melhorBeneficio;

    public SimAnnel() {
        itemNames = new String[]{"map", "compass", "water", "sandwich", "glucose", "tin", "banana", "apple",
            "cheese", "beer", "suntan cream", "camera", "T-shirt", "trousers", "umbrella", "waterproof trousers",
            "waterproof overclothes", "note-case", "sunglasses", "towel", "socks", "book"};
        pesos = new double[]{9, 13, 153, 50, 15, 68, 27, 39, 23, 52, 11, 32, 24, 48, 73,
            42, 43, 22, 7, 18, 4, 30};

        beneficios = new double[]{150, 35, 200, 160, 60, 45, 60, 40, 30, 10, 70, 30, 15, 10, 40,
            70, 75, 80, 20, 12, 50, 10};
        
        melhorSolucao = new int[itemNames.length];
        
        initialSolution();
    }

    private void initialSolution(){
        for(int i = 0; i < melhorSolucao.length; i++){
            melhorSolucao[i] = 0;
        }
        
        melhorBeneficio = calculateValue(melhorSolucao);
    }
    
    
    //<editor-fold defaultstate="collapsed" desc="ALGORITMO">
  
    public void run() {
        itAtual = 0;
        solucaoOriginal = melhorSolucao.clone();
        while (temperaturaAtual > temperaturaMinima) {
            while (itAtual < saMax) {
                itAtual++;
                //Gera vizinho aleatório
                int r = generateRandom();
                int[] solucaoTemp = solucaoOriginal.clone();
                if (solucaoTemp[r] == 0) {
                    solucaoTemp[r] = 1;
                } else {
                    solucaoTemp[r] = 0;
                }
                //System.out.println("solucaoTemp: " + Arrays.toString(solucaoTemp));
                //---------------------
                
                double variacao = calculateValue(solucaoTemp) - calculateValue(solucaoOriginal);
                //System.out.println("variavcao: " + variacao);
                if (variacao > 0 && calculateWeight(solucaoTemp) < pesoMax) { //Variação de custo maior que 0 quer dizer q temp é melhor q original
                    solucaoOriginal = solucaoTemp.clone();
                    double beneficioTemp = calculateValue(solucaoTemp);
                    //System.out.println("benTemp: " + beneficioTemp + " melBen: " + melhorBeneficio);
                    if (beneficioTemp > melhorBeneficio) {
                        melhorSolucao = solucaoTemp.clone();
                        melhorBeneficio = beneficioTemp;
                    }
                }else{
                    //Implementar função fórmula de aceitação
                    double x = zeroOne();
                    if(x < funcaoAceitacao(temperaturaAtual, variacao)){
                        //System.out.println("Aceitou");
                        solucaoOriginal = solucaoTemp.clone();
                    }
                }
            }
            temperaturaAtual = temperaturaAtual * fatorAlpha;
            itAtual = 0;
            //printBest();
        }
    }

    private double funcaoAceitacao(double temperatura, double variacao){
        variacao = - variacao;
        double expoente = variacao/temperatura;
        
        return Math.exp(expoente);
    }
    
    private int generateRandom() {
        return new Random().nextInt(itemNames.length);
    }

    private double zeroOne(){
        return new Random().nextDouble();
    }
    
    private double calculateValue(int[] solucao) {
        double value = 0;

        for (int i = 0; i < solucao.length; i++) {
            if (solucao[i] == 1) {
                value += beneficios[i];
            }
        }
        return value;
    }
    
    private double calculateWeight(int[] solucao){
        double peso = 0;

        for (int i = 0; i < solucao.length; i++) {
            if (solucao[i] == 1) {
                peso += pesos[i];
            }
        }
        return peso;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="PRINT">
    public void printBest(){
        System.out.println("Array: " + Arrays.toString(melhorSolucao));
        System.out.println("Melhor beneficio: " + melhorBeneficio);
        System.out.println("Peso Total: " + calculateWeight(melhorSolucao) + "\n");
    }
    //</editor-fold>
}
