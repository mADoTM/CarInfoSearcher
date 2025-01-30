package ru.dolzhenkoms.carinfosearcher.repository.jpa

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import ru.dolzhenkoms.carinfosearcher.model.jpa.HistoryEntity

interface HistoryEntityRepository : JpaRepository<HistoryEntity, Long> {

    @Query(
        nativeQuery = true,
        value = "WITH RankedRecords AS (\n" +
                "    SELECT\n" +
                "        history_entity.*,\n " +
                "        ROW_NUMBER() OVER (PARTITION BY field_name ORDER BY created_ts DESC) AS rn\n" +
                "    FROM\n" +
                "        history_entity\n" +
                "    WHERE\n" +
                "        vin = :vin AND source_type = :sourceType\n" +
                ")\n" +
                "SELECT\n" +
                "    *" +
                "FROM\n" +
                "    RankedRecords\n" +
                "WHERE\n" +
                "    rn = 1;"
    )
    fun findNewestByVinAndSourceType(vin: String, sourceType: String): List<HistoryEntity>

    fun findAllByVin(vin: String): List<HistoryEntity>
}