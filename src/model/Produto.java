package model;

/**
 * Classe que representa um produto no sistema.
 * Um produto tem código único, nome, preço e quantidade em estoque.
 */
public class Produto {
    // Código único do produto (ex: "001", "A123", etc.)
    private String codigo;
    
    // Nome ou descrição do produto
    private String nome;
    
    // Preço unitário do produto em reais
    private double preco;
    
    // Quantidade disponível em estoque
    private int estoque;
    
    /**
     * Construtor completo da classe Produto.
     * 
     * @param codigo Código único do produto
     * @param nome Nome ou descrição do produto
     * @param preco Preço unitário em reais
     * @param estoque Quantidade em estoque
     */
    public Produto(String codigo, String nome, double preco, int estoque) {
        this.codigo = codigo;
        this.nome = nome;
        this.preco = preco;
        this.estoque = estoque;
    }
    
    // Métodos de acesso (getters e setters)
    
    /**
     * Retorna o código do produto
     */
    public String getCodigo() {
        return codigo;
    }
    
    /**
     * Define o código do produto
     */
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
    
    /**
     * Retorna o nome do produto
     */
    public String getNome() {
        return nome;
    }
    
    /**
     * Define o nome do produto
     */
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    /**
     * Retorna o preço do produto
     */
    public double getPreco() {
        return preco;
    }
    
    /**
     * Define o preço do produto
     */
    public void setPreco(double preco) {
        this.preco = preco;
    }
    
    /**
     * Retorna a quantidade em estoque
     */
    public int getEstoque() {
        return estoque;
    }
    
    /**
     * Define a quantidade em estoque
     */
    public void setEstoque(int estoque) {
        this.estoque = estoque;
    }
    
    /**
     * Retorna uma representação textual do produto.
     * Útil para depuração e exibição de informações em logs.
     */
    @Override
    public String toString() {
        return "Produto [codigo=" + codigo + ", nome=" + nome + 
               ", preco=" + preco + ", estoque=" + estoque + "]";
    }
} 