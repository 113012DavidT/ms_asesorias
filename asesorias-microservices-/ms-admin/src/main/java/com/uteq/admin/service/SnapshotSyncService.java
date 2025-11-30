package com.uteq.admin.service;

import com.uteq.admin.client.MsDivisionesClient;
import com.uteq.admin.dto.DivisionDTO;
import com.uteq.admin.dto.ProgramaDTO;
import com.uteq.admin.entity.SnapshotDivision;
import com.uteq.admin.entity.SnapshotPrograma;
import com.uteq.admin.repository.SnapshotDivisionRepository;
import com.uteq.admin.repository.SnapshotProgramaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SnapshotSyncService {

    private final MsDivisionesClient msDivisionesClient;
    private final SnapshotDivisionRepository snapshotDivisionRepo;
    private final SnapshotProgramaRepository snapshotProgramaRepo;

    /**
     * Sincronizar todas las divisiones desde ms-divisiones
     */
    public void sincronizarDivisiones() {
        try {
            List<DivisionDTO> divisiones = msDivisionesClient.obtenerDivisiones();
            for (DivisionDTO divDTO : divisiones) {
                SnapshotDivision existing = snapshotDivisionRepo.findByDivisionesId(divDTO.getId());
                if (existing == null) {
                    // Crear nuevo snapshot
                    snapshotDivisionRepo.save(SnapshotDivision.builder()
                            .divisionesId(divDTO.getId())
                            .clave(divDTO.getClave())
                            .nombre(divDTO.getNombre())
                            .descripcion(divDTO.getDescripcion())
                            .build());
                } else {
                    // Actualizar existente
                    existing.setClave(divDTO.getClave());
                    existing.setNombre(divDTO.getNombre());
                    existing.setDescripcion(divDTO.getDescripcion());
                    snapshotDivisionRepo.save(existing);
                }
            }
            System.out.println("✅ Divisiones sincronizadas: " + divisiones.size());
        } catch (Exception e) {
            System.err.println("❌ Error sincronizando divisiones: " + e.getMessage());
        }
    }

    /**
     * Sincronizar todos los programas desde ms-divisiones
     */
    public void sincronizarProgramas() {
        try {
            List<ProgramaDTO> programas = msDivisionesClient.obtenerProgramas();
            for (ProgramaDTO progDTO : programas) {
                SnapshotPrograma existing = snapshotProgramaRepo.findByProgramasId(progDTO.getId());
                if (existing == null) {
                    // Crear nuevo snapshot
                    // Nota: necesitamos mapear el program.divisionId al snapshot de division
                    snapshotProgramaRepo.save(SnapshotPrograma.builder()
                            .programasId(progDTO.getId())
                            .clave(progDTO.getClave())
                            .nombre(progDTO.getNombre())
                            .descripcion(progDTO.getDescripcion())
                            .divisionId(1L)  // Placeholder - se actualiza cuando se conecte ms-divisiones
                            .build());
                } else {
                    // Actualizar existente
                    existing.setClave(progDTO.getClave());
                    existing.setNombre(progDTO.getNombre());
                    existing.setDescripcion(progDTO.getDescripcion());
                    snapshotProgramaRepo.save(existing);
                }
            }
            System.out.println("✅ Programas sincronizados: " + programas.size());
        } catch (Exception e) {
            System.err.println("❌ Error sincronizando programas: " + e.getMessage());
        }
    }

    /**
     * Obtener todas las divisiones del snapshot local
     */
    public List<DivisionDTO> obtenerDivisiones() {
        return snapshotDivisionRepo.findAll().stream()
                .map(snap -> DivisionDTO.builder()
                        .id(snap.getId())
                        .clave(snap.getClave())
                        .nombre(snap.getNombre())
                        .descripcion(snap.getDescripcion())
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * Obtener todos los programas del snapshot local
     */
    public List<ProgramaDTO> obtenerProgramas() {
        return snapshotProgramaRepo.findAll().stream()
                .map(snap -> ProgramaDTO.builder()
                        .id(snap.getId())
                        .clave(snap.getClave())
                        .nombre(snap.getNombre())
                        .descripcion(snap.getDescripcion())
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * Obtener programas de una división
     */
    public List<ProgramaDTO> obtenerProgramasPorDivision(Long divisionId) {
        return snapshotProgramaRepo.findByDivisionId(divisionId).stream()
                .map(snap -> ProgramaDTO.builder()
                        .id(snap.getId())
                        .clave(snap.getClave())
                        .nombre(snap.getNombre())
                        .descripcion(snap.getDescripcion())
                        .build())
                .collect(Collectors.toList());
    }
}
