package com.portoitapoa.faturamentofast.repository;

import com.portoitapoa.faturamentofast.entity.Siscomex;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * @author Fernando de Lima
 */
public interface SiscomexRepository extends JpaRepository<Siscomex, Integer> {

    Optional<Siscomex> findFirstByBlOriginalAndDataLiberacaoIsNotNull(String numeroBl);

    @Query(value = "SELECT sdi  FROM Siscomex sdi WHERE (sdi.parte = true) AND sdi.status = :status AND ((sdi.dataLiberacaoFatOnline  >= :dataLiberacaoMinima AND sdi.dataLiberacaoFatOnline <= :dataLiberacaoMaxima AND sdi.dataEnvioFatOnline IS NULL) " +
         "OR  (sdi.dataEnvioFatOnline  >= :dataEnvioFatOnline AND sdi.statusEnvioFatOnline  = 'ERRO'))")
    List<Siscomex> listarProcessosParte(@Param("status") String status, @Param("dataLiberacaoMinima") LocalDateTime dataLiberacaoMinima, @Param("dataLiberacaoMaxima") LocalDateTime dataLiberacaoMaxima, @Param("dataEnvioFatOnline") LocalDateTime dataEnvioFatOnline);

    @Query(value = "SELECT sdi FROM Siscomex sdi WHERE sdi.status = :status AND ((sdi.dataLiberacaoFatOnline >= :dataLiberacaoMinima AND sdi.dataLiberacaoFatOnline <= :dataLiberacaoMaxima AND sdi.dataEnvioFatOnline IS NULL) " +
            "OR (sdi.dataEnvioFatOnline >= :dataEnvioFatOnline AND sdi.statusEnvioFatOnline = 'ERRO'))")
    List<Siscomex> findAllForProcess(@Param("status") String status,  @Param("dataLiberacaoMinima") LocalDateTime dataLiberacaoMinima, @Param("dataLiberacaoMaxima") LocalDateTime dataLiberacaoMaxima, @Param("dataEnvioFatOnline") LocalDateTime dataEnvioFatOnline);

    @Query(value = "SELECT sdi  FROM Siscomex sdi WHERE sdi.parte = :parte AND sdi.crossdocking = 1 AND sdi.status = :status AND (sdi.dataLiberacao  >= :dataLiberacao AND sdi.dataEnvioFatOnline IS NULL) " +
            "OR  (sdi.dataEnvioFatOnline >= :dataEnvioFatOnline AND sdi.statusEnvioFatOnline  = 'ERRO')")
    List<Siscomex> findAllForProcessCrossDocking(@Param("parte") Boolean parte, @Param("status") String status,  @Param("dataLiberacao") LocalDateTime dataLiberacao, @Param("dataEnvioFatOnline") LocalDateTime dataEnvioFatOnline);

}
