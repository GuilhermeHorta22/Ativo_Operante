package ativo_operante.ativooperante_be.repositories;

import ativo_operante.ativooperante_be.entities.Tipo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//A classe repository serve para manipular uma classe especifica
//JpaRepository -> permite usar alguns metodos prontos
//Tipo -> indica a classe que vamos manipular
//Long -> indica o tipo primitivo da chave primaria da classe Tipo
@Repository
public interface TipoRepository extends JpaRepository<Tipo, Long> {
}
