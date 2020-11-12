package com.example.jaimequeraltgarrigos.kotlinmovieapp.utils.mapper

interface EntityMapper<Entity, DomainModel> {
    fun entityToModel(entity: Entity): DomainModel
    fun modelToEntity(model: DomainModel): Entity
}