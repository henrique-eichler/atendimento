package com.clinica.atendimento.dto

import com.clinica.atendimento.model.Convenio
import com.clinica.atendimento.model.Terapia
import com.clinica.atendimento.model.TerapiaConvenio
import com.fasterxml.jackson.annotation.JsonProperty

data class TerapiaConvenioDTO(
    @JsonProperty
    var id: Long? = null,
    
    @JsonProperty
    var terapia: TerapiaDTO? = null,
    
    @JsonProperty
    var convenio: ConvenioDTO? = null,
    
    @JsonProperty
    var valorTerapia: Float? = null,
    
    @JsonProperty
    var valorProfissional: Float? = null
) {
    // Fluent accessors to maintain compatibility with Java code
    fun id(): Long? = id
    
    fun id(id: Long?): TerapiaConvenioDTO {
        this.id = id
        return this
    }
    
    fun terapia(): TerapiaDTO? = terapia
    
    fun terapia(terapia: TerapiaDTO?): TerapiaConvenioDTO {
        this.terapia = terapia
        return this
    }
    
    fun convenio(): ConvenioDTO? = convenio
    
    fun convenio(convenio: ConvenioDTO?): TerapiaConvenioDTO {
        this.convenio = convenio
        return this
    }
    
    fun valorTerapia(): Float? = valorTerapia
    
    fun valorTerapia(valorTerapia: Float?): TerapiaConvenioDTO {
        this.valorTerapia = valorTerapia
        return this
    }
    
    fun valorProfissional(): Float? = valorProfissional
    
    fun valorProfissional(valorProfissional: Float?): TerapiaConvenioDTO {
        this.valorProfissional = valorProfissional
        return this
    }

    // Method to convert from DTO to entity
    fun toEntity(): TerapiaConvenio {
        val terapiaEntity = terapia?.toEntity()
        val convenioEntity = convenio?.toEntity()

        return TerapiaConvenio.builder()
            .id(id)
            .terapia(terapiaEntity)
            .convenio(convenioEntity)
            .valorTerapia(valorTerapia)
            .valorProfissional(valorProfissional)
            .build()
    }

    companion object {
        // Static method to convert from entity to DTO
        @JvmStatic
        fun fromEntity(terapiaConvenio: TerapiaConvenio?): TerapiaConvenioDTO? {
            if (terapiaConvenio == null) {
                return null
            }

            return TerapiaConvenioDTO(
                id = terapiaConvenio.id(),
                terapia = TerapiaDTO.fromEntity(terapiaConvenio.terapia()),
                convenio = ConvenioDTO.fromEntity(terapiaConvenio.convenio()),
                valorTerapia = terapiaConvenio.valorTerapia(),
                valorProfissional = terapiaConvenio.valorProfissional()
            )
        }

        @JvmStatic
        fun builder(): TerapiaConvenioDTOBuilder {
            return TerapiaConvenioDTOBuilder()
        }
    }

    class TerapiaConvenioDTOBuilder {
        private val instance = TerapiaConvenioDTO()

        fun id(id: Long?): TerapiaConvenioDTOBuilder {
            instance.id = id
            return this
        }

        fun terapia(terapia: TerapiaDTO?): TerapiaConvenioDTOBuilder {
            instance.terapia = terapia
            return this
        }

        fun convenio(convenio: ConvenioDTO?): TerapiaConvenioDTOBuilder {
            instance.convenio = convenio
            return this
        }

        fun valorTerapia(valorTerapia: Float?): TerapiaConvenioDTOBuilder {
            instance.valorTerapia = valorTerapia
            return this
        }

        fun valorProfissional(valorProfissional: Float?): TerapiaConvenioDTOBuilder {
            instance.valorProfissional = valorProfissional
            return this
        }

        fun build(): TerapiaConvenioDTO {
            return instance
        }
    }
}