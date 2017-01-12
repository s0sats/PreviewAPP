package com.namoadigital.prj001.model;

/**
 * Created by neomatrix on 4/20/16.
 */
public class Parametro {

    private long parametro_code;
    private String nome;
    private String descricao;
    private String valor_default;
    private String valor_customizado;

    public long getParametro_code() {
        return parametro_code;
    }

    public void setParametro_code(long parametro_code) {
        this.parametro_code = parametro_code;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getValor_default() {
        return valor_default;
    }

    public void setValor_default(String valor_default) {
        this.valor_default = valor_default;
    }

    public String getValor_customizado() {
        return valor_customizado;
    }

    public void setValor_customizado(String valor_customizado) {
        this.valor_customizado = valor_customizado;
    }
}
