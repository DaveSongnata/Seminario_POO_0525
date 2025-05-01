package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Classe que representa uma venda no sistema.
 */
public class Venda {
    private String numero;
    private ArrayList<ItemVenda> itens;
    private LocalDateTime data;
    private boolean cancelada;
    
    /**
     * Construtor que inicializa uma nova venda com um número único
     */
    public Venda() {
        this.numero = UUID.randomUUID().toString();
        this.itens = new ArrayList<>();
        this.data = LocalDateTime.now();
        this.cancelada = false;
    }
    
    // Getters e Setters
    public String getNumero() {
        return numero;
    }
    
    public ArrayList<ItemVenda> getItens() {
        return itens;
    }
    
    public LocalDateTime getData() {
        return data;
    }
    
    public boolean isCancelada() {
        return cancelada;
    }
    
    /**
     * Adiciona um item à venda
     */
    public void adicionarItem(Produto produto, int quantidade) {
        itens.add(new ItemVenda(produto, quantidade));
    }
    
    /**
     * Calcula o valor total da venda
     */
    public double calcularTotal() {
        return itens.stream()
                .mapToDouble(ItemVenda::getValorTotal)
                .sum();
    }
    
    /**
     * Cancela a venda
     */
    public void cancelar() {
        this.cancelada = true;
    }
    
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return "Venda [numero=" + numero + ", data=" + data.format(formatter) + 
                ", cancelada=" + cancelada + ", total=" + calcularTotal() + "]";
    }
} 