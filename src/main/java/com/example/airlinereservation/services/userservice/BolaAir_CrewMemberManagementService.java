package com.example.airlinereservation.services.userservice;

import com.example.airlinereservation.data.model.enums.Destinations;
import com.example.airlinereservation.data.model.persons.CrewMember;
import com.example.airlinereservation.dtos.response.users.CreateCrewMemberResponse;
import com.example.airlinereservation.dtos.response.users.CrewMemberResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class BolaAir_CrewMemberManagementService implements CrewMemberManagementService{

    private final Set<CrewMember> crewMembersDepartment = new HashSet<>();

    @Override
    public CreateCrewMemberResponse addCrewMemberToDepartment(CrewMember crewMember) {
        crewMember.setAvailable(true);
        crewMembersDepartment.add(crewMember);
        CreateCrewMemberResponse response = new CreateCrewMemberResponse();
        response.setDepartmentId(crewMember.getDepartmentId().toString());
        return response;
    }

    @Override
    public void removeCrewMemberFromDepartment(CrewMember crewMember) {
        crewMembersDepartment.removeIf(member-> member.equals(crewMember));
    }

    @Override
    public List<CrewMember> getAvailableCrewMembersBy(Destinations location, boolean availability) {
        return null;
    }
    
    @Override
    public CrewMemberResponse assignCrewMember(CrewMember crewMember, Destinations location) {
        return null;
    }
    
    @Override
    public List<CrewMemberResponse> assignCrewMembers(int sizeOfCrewMembers, Destinations location) {
        return null;
    }
    
    
}
