package com.example.demo.controller;

import com.example.demo.model.Agent;
import com.example.demo.repository.AgentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/agents")
public class AgentController {

    @Autowired
    private AgentRepository agentRepository;

    // Create a new agent
    @PostMapping
    public Agent createAgent(@RequestBody Agent agent) {
        return agentRepository.save(agent);
    }



    // Get all agents
    @GetMapping
    public List<Agent> getAllAgents() {
        return agentRepository.findAll();
    }

    // Get agent by ID
    @GetMapping("/{id}")
    public ResponseEntity<Agent> getAgentById(@PathVariable Integer id) {
        return agentRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Update agent
    @PutMapping("/{id}")
    public ResponseEntity<Agent> updateAgent(@PathVariable Integer id, @RequestBody Agent agentDetails) {
        return agentRepository.findById(id)
                .map(agent -> {
                    agent.setFirstName(agentDetails.getFirstName());
                    agent.setLastName(agentDetails.getLastName());
                    agent.setDepartment(agentDetails.getDepartment());
                    return ResponseEntity.ok(agentRepository.save(agent));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Delete agent
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAgent(@PathVariable Integer id) {
        return agentRepository.findById(id)
                .map(agent -> {
                    agentRepository.delete(agent);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
