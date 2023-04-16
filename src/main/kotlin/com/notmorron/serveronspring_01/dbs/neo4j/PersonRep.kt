package com.notmorron.serveronspring_01.dbs.neo4j

import com.notmorron.serveronspring_01.dbs.neo4j.models.Person
import org.springframework.data.neo4j.repository.Neo4jRepository
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories


@EnableNeo4jRepositories
interface PersonRep : Neo4jRepository<Person, Long>