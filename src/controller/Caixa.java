package controller;

import java.util.ArrayList;
import java.util.Iterator;
import model.CodigoDuplicadoException;
import model.EstoqueInsuficienteException;
import model.ItemVenda;
import model.Produto;
import model.Venda;

/**
 * Controlador principal do sistema de caixa.
 * Esta classe gerencia produtos e vendas, sendo o centro do padrão MVC.
 */
public class Caixa {
    // Lista de produtos cadastrados
    private ArrayList<Produto> produtos;
    
    // Lista de vendas realizadas
    private ArrayList<Venda> vendas;
    
    // Saldo total do caixa
    private double saldoCaixa;
    
    /**
     * Construtor que inicializa as listas e o saldo do caixa
     */
    public Caixa() {
        this.produtos = new ArrayList<>();
        this.vendas = new ArrayList<>();
        this.saldoCaixa = 0.0;
    }
    
    // Métodos de acesso (Getters)
    
    /**
     * Retorna a lista de produtos cadastrados
     */
    public ArrayList<Produto> getProdutos() {
        return produtos;
    }
    
    /**
     * Retorna a lista de vendas realizadas
     */
    public ArrayList<Venda> getVendas() {
        return vendas;
    }
    
    /**
     * Retorna o saldo atual do caixa
     */
    public double getSaldoCaixa() {
        return saldoCaixa;
    }
    
    /**
     * Cadastra um novo produto, validando se o código já existe
     * 
     * @param novo Produto a ser cadastrado
     * @throws CodigoDuplicadoException Se já existe um produto com o mesmo código
     */
    public void cadastrarProduto(Produto novo) throws CodigoDuplicadoException {
        // Verifica se já existe produto com o mesmo código
        if (produtos.stream().anyMatch(p -> p.getCodigo().equals(novo.getCodigo()))) {
            throw new CodigoDuplicadoException("Código já existe: " + novo.getCodigo());
        }
        // Adiciona o produto na lista
        produtos.add(novo);
    }
    
    /**
     * Busca um produto pelo código
     * 
     * @param codigo Código do produto a ser buscado
     * @return O produto encontrado ou null se não existir
     */
    public Produto buscarProduto(String codigo) {
        // Utiliza Stream API para buscar o produto
        return produtos.stream()
                .filter(p -> p.getCodigo().equals(codigo))
                .findFirst()
                .orElse(null);
    }
    
    /**
     * Realiza uma venda, atualizando o estoque dos produtos
     * 
     * @param venda Venda a ser realizada
     * @throws EstoqueInsuficienteException Se não houver estoque suficiente
     */
    public void realizarVenda(Venda venda) throws EstoqueInsuficienteException {
        // Verificar se há estoque suficiente para todos os itens
        for (ItemVenda item : venda.getItens()) {
            Produto p = item.getProduto();
            if (p.getEstoque() < item.getQuantidade()) {
                throw new EstoqueInsuficienteException("Estoque insuficiente para o produto: " + p.getNome());
            }
            // Atualizar o estoque do produto
            p.setEstoque(p.getEstoque() - item.getQuantidade());
        }
        
        // Adicionar a venda à lista
        vendas.add(venda);
        
        // Atualizar o saldo do caixa
        saldoCaixa += venda.calcularTotal();
    }
    
    /**
     * Cancela uma venda pelo número (ID)
     * 
     * @param numero Número da venda a ser cancelada
     */
    public void cancelarVenda(String numero) {
        // Usar Iterator para percorrer a lista de forma segura
        Iterator<Venda> iterator = vendas.iterator();
        
        while (iterator.hasNext()) {
            Venda venda = iterator.next();
            // Encontrou a venda e ela não está cancelada
            if (venda.getNumero().equals(numero) && !venda.isCancelada()) {
                // Marcar a venda como cancelada
                venda.cancelar();
                
                // Devolver produtos ao estoque
                for (ItemVenda item : venda.getItens()) {
                    Produto p = item.getProduto();
                    p.setEstoque(p.getEstoque() + item.getQuantidade());
                }
                
                // Remover valor da venda do saldo do caixa
                saldoCaixa -= venda.calcularTotal();
                break;
            }
        }
    }
} 