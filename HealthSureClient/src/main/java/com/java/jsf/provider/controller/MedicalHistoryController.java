package com.java.jsf.provider.controller;

import java.util.*;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import com.java.jsf.provider.dao.DoctorAvailabilityDao;
import com.java.jsf.provider.dao.MedicalHistoryDao;
import com.java.jsf.provider.model.MedicalProcedure;
import com.java.jsf.provider.model.Prescription;
import com.java.jsf.provider.model.ProcedureType;

public class MedicalHistoryController {

    private String doctorId;
    private String searchKey;
    private String searchType = "";
    private String nameSearchMode = "";

    private List<MedicalProcedure> searchResults;
    private MedicalProcedure medicalProcedure;

    private MedicalHistoryDao medicalHistoryDao;
    private DoctorAvailabilityDao availabilityDao;

    private String sortColumn = "";
    private boolean sortAscending = true;
    private int currentPage = 0;
    private int pageSize = 5;

    private List<Prescription> prescriptionList;
    private String prescriptionSortColumn = "";
    private boolean prescriptionSortAscending = true;
    private int prescriptionCurrentPage = 0;
    private int prescriptionPageSize = 5;

    public String searchProcedures() {
        FacesContext context = FacesContext.getCurrentInstance();

        if (doctorId == null || doctorId.trim().isEmpty()) {
            context.addMessage("historyForm:doctorId", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Doctor ID is required.", null));
            return null;
        }

        doctorId = doctorId.trim().toUpperCase();

        if (!doctorId.matches("^DOC\\d{3}$")) {
            context.addMessage("historyForm:doctorId", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Doctor ID must be in format DOC001.", null));
            return null;
        }

        if (!medicalHistoryDao.doctorExists(doctorId)) {
            context.addMessage("historyForm:doctorId", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Doctor ID does not exist.", null));
            return null;
        }

        if (searchType == null || searchType.trim().isEmpty()) {
            searchResults = medicalHistoryDao.getAllProceduresByDoctor(doctorId);
        } else {
            if (searchKey == null || searchKey.trim().isEmpty()) {
                context.addMessage("historyForm:searchKey", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Search value cannot be empty.", null));
                return null;
            }

            String trimmedKey = searchKey.trim();

            switch (searchType) {
                case "hid":
                    String normalizedHid = trimmedKey.toUpperCase();
                    if (!normalizedHid.matches("^HID\\d{3}$")) {
                        context.addMessage("historyForm:searchKey", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid HID. Format: HID001", null));
                        return null;
                    }
                    searchResults = medicalHistoryDao.searchByHid(doctorId, normalizedHid);
                    if (searchResults == null || searchResults.isEmpty()) {
                        context.addMessage("historyForm:searchKey", new FacesMessage(FacesMessage.SEVERITY_WARN, "No procedures found. HID may not belong to the doctor.", null));
                        searchResults = medicalHistoryDao.getAllProceduresByDoctor(doctorId);
                    }
                    break;

                case "name":
                    if (nameSearchMode == null || nameSearchMode.trim().isEmpty()) {
                        context.addMessage("historyForm:nameSearchMode", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Select Name search mode.", null));
                        return null;
                    }

                    if ("startsWith".equals(nameSearchMode)) {
                        searchResults = medicalHistoryDao.searchByNameStartsWith(doctorId, trimmedKey);
                    } else if ("contains".equals(nameSearchMode)) {
                        searchResults = medicalHistoryDao.searchByNameContains(doctorId, trimmedKey);
                    } else {
                        context.addMessage("historyForm:nameSearchMode", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid name search mode.", null));
                        return null;
                    }

                    if (searchResults == null || searchResults.isEmpty()) {
                        context.addMessage("historyForm:searchKey", new FacesMessage(FacesMessage.SEVERITY_WARN, "No recipient found under this doctor with given name.", null));
                        searchResults = medicalHistoryDao.getAllProceduresByDoctor(doctorId);
                    }
                    break;

                case "mobile":
                    if (!trimmedKey.matches("^[6-9]\\d{9}$")) {
                        context.addMessage("historyForm:searchKey", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid phone number.", null));
                        return null;
                    }

                    searchResults = medicalHistoryDao.searchByMobile(doctorId, trimmedKey);

                    if (searchResults == null || searchResults.isEmpty()) {
                        context.addMessage("historyForm:searchKey", new FacesMessage(FacesMessage.SEVERITY_WARN, "No recipient found under this doctor with this phone.", null));
                        searchResults = medicalHistoryDao.getAllProceduresByDoctor(doctorId);
                    }
                    break;

                default:
                    context.addMessage("historyForm:searchType", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid search type.", null));
                    return null;
            }
        }

        sortResults();
        resetPagination();
        return null;
    }

    public boolean getShowDurationDates() {
        return searchResults != null && searchResults.stream().anyMatch(p -> p.getType() == ProcedureType.LONG_TERM);
    }

    public String confirmSearchType() {
        if (!"name".equals(searchType)) {
            nameSearchMode = "";
        }
        return null;
    }

    public String resetForm() {
        doctorId = null;
        searchKey = null;
        searchType = "";
        nameSearchMode = "";
        searchResults = null;
        sortColumn = "";
        sortAscending = true;
        currentPage = 0;
        prescriptionList = null;
        medicalProcedure = null;
        return "MedicalProcedureSearch?faces-redirect=true";
    }

    public String sortBy(String column) {
        if (column.equals(sortColumn)) {
            sortAscending = !sortAscending;
        } else {
            sortColumn = column;
            sortAscending = true;
        }
        sortResults();
        resetPagination();
        return null;
    }

    private void sortResults() {
        if (searchResults == null) return;
        searchResults.sort((p1, p2) -> {
            int result = 0;
            switch (sortColumn) {
                case "procedureId": result = p1.getProcedureId().compareTo(p2.getProcedureId()); break;
                case "procedureDate": result = p1.getProcedureDate().compareTo(p2.getProcedureDate()); break;
                case "diagnosis": result = p1.getDiagnosis().compareTo(p2.getDiagnosis()); break;
                case "recommendations": result = p1.getRecommendations().compareTo(p2.getRecommendations()); break;
                case "recipientName":
                    result = (p1.getRecipient().getFirstName() + p1.getRecipient().getLastName())
                            .compareTo(p2.getRecipient().getFirstName() + p2.getRecipient().getLastName());
                    break;
                case "fromDate":
                    result = Objects.compare(p1.getFromDate(), p2.getFromDate(), Comparator.naturalOrder());
                    break;
                case "toDate":
                    result = Objects.compare(p1.getToDate(), p2.getToDate(), Comparator.naturalOrder());
                    break;
            }
            return sortAscending ? result : -result;
        });
    }

    public List<MedicalProcedure> getPaginatedList() {
        if (searchResults == null) return null;
        int start = currentPage * pageSize;
        int end = Math.min(start + pageSize, searchResults.size());
        return searchResults.subList(start, end);
    }

    public int getTotalPages() {
        return (searchResults == null || searchResults.isEmpty()) ? 1 :
                (int) Math.ceil((double) searchResults.size() / pageSize);
    }

    public boolean isNextButtonDisabled() {
        return searchResults == null || ((currentPage + 1) * pageSize) >= searchResults.size();
    }

    public boolean isPreviousButtonDisabled() {
        return currentPage == 0;
    }

    public String nextPage() {
        if (currentPage < getTotalPages() - 1) currentPage++;
        return null;
    }

    public String previousPage() {
        if (currentPage > 0) currentPage--;
        return null;
    }

    public void resetPagination() {
        currentPage = 0;
    }

    public String loadPrescriptions(MedicalProcedure procedure) {
        this.medicalProcedure = procedure;
        this.prescriptionList = new ArrayList<>(procedure.getPrescription());
        sortPrescriptionResults();
        resetPrescriptionPagination();
        return null;
    }

    public String sortPrescriptionsBy(String column) {
        if (column.equals(prescriptionSortColumn)) {
            prescriptionSortAscending = !prescriptionSortAscending;
        } else {
            prescriptionSortColumn = column;
            prescriptionSortAscending = true;
        }
        sortPrescriptionResults();
        resetPrescriptionPagination();
        return null;
    }

    private void sortPrescriptionResults() {
        if (prescriptionList == null) return;
        prescriptionList.sort((p1, p2) -> {
            int result = 0;
            switch (prescriptionSortColumn) {
                case "prescriptionId": result = p1.getPrescriptionId().compareTo(p2.getPrescriptionId()); break;
                case "doctorName": result = p1.getDoctor().getDoctorName().compareTo(p2.getDoctor().getDoctorName()); break;
                case "writtenOn": result = p1.getWrittenOn().compareTo(p2.getWrittenOn()); break;
                case "startDate": result = p1.getStartDate().compareTo(p2.getStartDate()); break;
                case "endDate": result = p1.getEndDate().compareTo(p2.getEndDate()); break;
            }
            return prescriptionSortAscending ? result : -result;
        });
    }

    public List<Prescription> getPaginatedPrescriptions() {
        if (prescriptionList == null) return Collections.emptyList();
        int start = prescriptionCurrentPage * prescriptionPageSize;
        int end = Math.min(start + prescriptionPageSize, prescriptionList.size());
        return prescriptionList.subList(start, end);
    }

    public int getTotalPrescriptionPages() {
        return (prescriptionList == null || prescriptionList.isEmpty()) ? 1 :
                (int) Math.ceil((double) prescriptionList.size() / prescriptionPageSize);
    }

    public String nextPrescriptionPage() {
        if ((prescriptionCurrentPage + 1) * prescriptionPageSize < prescriptionList.size()) {
            prescriptionCurrentPage++;
        }
        return null;
    }

    public String previousPrescriptionPage() {
        if (prescriptionCurrentPage > 0) {
            prescriptionCurrentPage--;
        }
        return null;
    }

    public void resetPrescriptionPagination() {
        prescriptionCurrentPage = 0;
    }

    // === Getters and Setters ===

    
	public String getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(String doctorId) {
		this.doctorId = doctorId;
	}

	public String getSearchKey() {
		return searchKey;
	}

	public void setSearchKey(String searchKey) {
		this.searchKey = searchKey;
	}

	public String getSearchType() {
		return searchType;
	}

	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}

	public String getNameSearchMode() {
		return nameSearchMode;
	}

	public void setNameSearchMode(String nameSearchMode) {
		this.nameSearchMode = nameSearchMode;
	}

	public List<MedicalProcedure> getSearchResults() {
		return searchResults;
	}

	public void setSearchResults(List<MedicalProcedure> searchResults) {
		this.searchResults = searchResults;
	}

	public MedicalProcedure getMedicalProcedure() {
		return medicalProcedure;
	}

	public void setMedicalProcedure(MedicalProcedure medicalProcedure) {
		this.medicalProcedure = medicalProcedure;
	}

	public MedicalHistoryDao getMedicalHistoryDao() {
		return medicalHistoryDao;
	}

	public void setMedicalHistoryDao(MedicalHistoryDao medicalHistoryDao) {
		this.medicalHistoryDao = medicalHistoryDao;
	}

	public DoctorAvailabilityDao getAvailabilityDao() {
		return availabilityDao;
	}

	public void setAvailabilityDao(DoctorAvailabilityDao availabilityDao) {
		this.availabilityDao = availabilityDao;
	}

	public String getSortColumn() {
		return sortColumn;
	}

	public void setSortColumn(String sortColumn) {
		this.sortColumn = sortColumn;
	}

	public boolean isSortAscending() {
		return sortAscending;
	}

	public void setSortAscending(boolean sortAscending) {
		this.sortAscending = sortAscending;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public List<Prescription> getPrescriptionList() {
		return prescriptionList;
	}

	public void setPrescriptionList(List<Prescription> prescriptionList) {
		this.prescriptionList = prescriptionList;
	}

	public String getPrescriptionSortColumn() {
		return prescriptionSortColumn;
	}

	public void setPrescriptionSortColumn(String prescriptionSortColumn) {
		this.prescriptionSortColumn = prescriptionSortColumn;
	}

	public boolean isPrescriptionSortAscending() {
		return prescriptionSortAscending;
	}

	public void setPrescriptionSortAscending(boolean prescriptionSortAscending) {
		this.prescriptionSortAscending = prescriptionSortAscending;
	}

	public int getPrescriptionCurrentPage() {
		return prescriptionCurrentPage;
	}

	public void setPrescriptionCurrentPage(int prescriptionCurrentPage) {
		this.prescriptionCurrentPage = prescriptionCurrentPage;
	}

	public int getPrescriptionPageSize() {
		return prescriptionPageSize;
	}

	public void setPrescriptionPageSize(int prescriptionPageSize) {
		this.prescriptionPageSize = prescriptionPageSize;
	}

}
