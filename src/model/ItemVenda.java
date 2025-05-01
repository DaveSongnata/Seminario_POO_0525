package model;

/**
 * Classe que representa um item de venda no sistema.
 */
public class ItemVenda {
    private Produto produto;
    private int quantidade;
    
    /**
     * Construtor completo da classe ItemVenda
     */
    public ItemVenda(Produto produto, int quantidade) {
        this.produto = produto;
        this.quantidade = quantidade;
    }
    
    // Getters e Setters
    public Produto getProduto() {
        return produto;
    }
    
    public void setProduto(Produto produto) {
        this.produto = produto;
    }
    
    public int getQuantidade() {
        return quantidade;
    }
    
    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }
    
    /**
     * Calcula o valor total do item (preço * quantidade)
     */
    public double getValorTotal() {
        return produto.getPreco() * quantidade;
    }
    
    @Override
    public String toString() {
        return "ItemVenda [produto=" + produto.getNome() + ", quantidade=" + quantidade + 
                ", valor unitário=" + produto.getPreco() + ", valor total=" + getValorTotal() + "]";
    }
} 