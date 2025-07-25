package com.java.jsf.provider.controller;

import java.util.*;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import com.java.jsf.provider.dao.DoctorAvailabilityDao;
import com.java.jsf.provider.dao.MedicalHistoryDao;
import com.java.jsf.provider.model.MedicalProcedure;
import com.java.jsf.provider.model.PrescribedMedicines;
import com.java.jsf.provider.model.Prescription;
import com.java.jsf.provider.model.ProcedureDailyLog;
import com.java.jsf.provider.model.ProcedureTest;

public class MedicalHistoryController {

    private String doctorId;
    private String searchKey;
    private String searchType = "";
    private String nameSearchMode = "";
    private String procedureTypeSelected;

    //----------------Medical Procedure-------------------
    
    private List<MedicalProcedure> searchResults;
    private MedicalProcedure medicalProcedure;

    private MedicalHistoryDao medicalHistoryDao;

    private String sortColumn = "";
    private boolean sortAscending = true;
    
    private int currentPage = 1;
    private int pageSize = 5;

    //----------------Prescription-------------------
    
    private List<Prescription> allPrescriptions;
    private List<Prescription> prescriptionList;
    private Prescription selectedPrescription;

    private int prescriptionCurrentPage = 1;
    private int prescriptionPageSize = 5;
    private int prescriptionTotalPages;

    private String prescriptionSortColumn = "";
    private boolean prescriptionSortAscending = true;
    
    //----------------Prescribed Medicines-------------------

    private List<PrescribedMedicines> prescribedMedicines;
    private List<PrescribedMedicines> allMedicines = new ArrayList<>();

    private int medicineCurrentPage = 1;
    private int medicinePageSize = 5;

    private String medicineSortColumn = "";
    private boolean medicineSortAscending = true;
    
    //----------------Prescribed Tests-------------------
    
    private List<ProcedureTest> prescribedTests;
    
    private String testsSortColumn = "";
    private boolean testsSortAscending = true;

    private int testCurrentPage = 1;
    private int testPageSize = 5;
    
    //----------------Procedure Daily Log-------------------
    
    private List<ProcedureDailyLog> allLogs;
    
    private int logCurrentPage = 1;
    private int logPageSize = 5;

    private String logSortColumn = "";
    private boolean logSortAscending = true;
    
    

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

        if (procedureTypeSelected == null || procedureTypeSelected.trim().isEmpty()) {
            context.addMessage("historyForm:procedureType", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please select a procedure type.", null));
            return null;
        }

        procedureTypeSelected = procedureTypeSelected.trim();

        if (searchType == null || searchType.trim().isEmpty()) {
            // Basic doctor + type search
            searchResults = medicalHistoryDao.searchByDoctorIdWithDetails(doctorId, procedureTypeSelected);

            if (searchResults == null || searchResults.isEmpty()) {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "No procedures found for selected doctor and procedure type.", null));
                return null;
            }
        } else {
            // Filtered search
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
                    searchResults = medicalHistoryDao.searchByHid(doctorId, normalizedHid, procedureTypeSelected);
                    break;

                case "name":
                    if (nameSearchMode == null || nameSearchMode.trim().isEmpty()) {
                        context.addMessage("historyForm:nameSearchMode", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Select Name search mode.", null));
                        return null;
                    }
                    if ("startsWith".equals(nameSearchMode)) {
                        searchResults = medicalHistoryDao.searchByNameStartsWith(doctorId, trimmedKey, procedureTypeSelected);
                    } else if ("contains".equals(nameSearchMode)) {
                        searchResults = medicalHistoryDao.searchByNameContains(doctorId, trimmedKey, procedureTypeSelected);
                    } else {
                        context.addMessage("historyForm:nameSearchMode", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid name search mode.", null));
                        return null;
                    }
                    break;

                case "mobile":
                    if (!trimmedKey.matches("^[6-9]\\d{9}$")) {
                        context.addMessage("historyForm:searchKey", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Phone Number Doesn't Exist.", null));
                        return null;
                    }
                    searchResults = medicalHistoryDao.searchByMobile(doctorId, trimmedKey, procedureTypeSelected);
                    break;

                default:
                    context.addMessage("historyForm:searchType", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid search type.", null));
                    return null;
            }

            if (searchResults == null || searchResults.isEmpty()) {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "No procedures found for this search.", null));
                return null;
            }
        }

        resetPagination();
        return null;
    }



    public String confirmSearchType() {
        if (!"name".equals(searchType)) {
            nameSearchMode = "";
        }
        return null;
    }

    public String resetForm() {
        this.doctorId = null;
        this.searchKey = null;
        this.searchType = null;
        this.nameSearchMode = null;
        this.searchResults = null;
        this.sortColumn = null;
        this.sortAscending = true;
        this.currentPage = 0;
        this.prescriptionList = null;
        this.medicalProcedure = null;
        this.procedureTypeSelected = null;
        return null;
    }

    public void resetPagination() {
        currentPage = 1;
    }

    public String sortBy(String column) {
        if (column.equals(this.sortColumn)) {
            this.sortAscending = !this.sortAscending;
        } else {
            this.sortColumn = column;
            this.sortAscending = true;
        }

        if (searchResults != null) {
            Comparator<MedicalProcedure> comparator = null;

            switch (column) {
                case "procedureId":
                    comparator = Comparator.comparing(MedicalProcedure::getProcedureId, Comparator.nullsLast(String::compareTo));
                    break;
                case "procedureDate":
                    comparator = Comparator.comparing(MedicalProcedure::getProcedureDate, Comparator.nullsLast(Date::compareTo));
                    break;
                case "diagnosis":
                    comparator = Comparator.comparing(MedicalProcedure::getDiagnosis, Comparator.nullsLast(String::compareToIgnoreCase));
                    break;
                case "recommendations":
                    comparator = Comparator.comparing(MedicalProcedure::getRecommendations, Comparator.nullsLast(String::compareToIgnoreCase));
                    break;
                case "recipientName":
                    comparator = Comparator.comparing(
                            p -> (p.getRecipient() != null)
                                    ? (p.getRecipient().getFirstName() + p.getRecipient().getLastName())
                                    : "",
                            Comparator.nullsLast(String::compareToIgnoreCase));
                    break;
                case "fromDate":
                    comparator = Comparator.comparing(MedicalProcedure::getFromDate, Comparator.nullsLast(Date::compareTo));
                    break;
                case "toDate":
                    comparator = Comparator.comparing(MedicalProcedure::getToDate, Comparator.nullsLast(Date::compareTo));
                    break;
            }

            if (comparator != null) {
                if (!sortAscending) {
                    comparator = comparator.reversed();
                }
                searchResults.sort(comparator);
            }
        }

        // Reset pagination
        currentPage = 1;
        getPaginatedList();
        return null;
    }

    public List<MedicalProcedure> getPaginatedList() {
        if (searchResults == null) return null;
        int start = (currentPage - 1) * pageSize;
        int end = Math.min(start + pageSize, searchResults.size());
        return searchResults.subList(start, end);
    }

    public int getTotalProcedureRecords() {
        return (searchResults != null) ? searchResults.size() : 0;
    }
    
    public int getTotalPages() {
        return (searchResults == null || searchResults.isEmpty()) ? 1 :
                (int) Math.ceil((double) searchResults.size() / pageSize);
    }

    public void goToFirstProcedurePage() {
        currentPage = 1;
    }

    public void goToLastProcedurePage() {
        currentPage = getTotalPages();
    }
    public boolean isNextButtonDisabled() {
        return searchResults == null || ((currentPage + 1) * pageSize) >= searchResults.size();
    }

    public boolean isPreviousButtonDisabled() {
        return currentPage == 1;
    }

    public String nextPage() {
        if (currentPage < getTotalPages()) currentPage++;
        return null;
    }

    public String previousPage() {
        if (currentPage > 1) currentPage--;
        return null;
    }

    public String loadPrescriptions(MedicalProcedure procedure) {
        FacesContext context = FacesContext.getCurrentInstance();
        
        // Initialize collections
        this.prescriptionList = new ArrayList<>();
        this.prescribedMedicines = new ArrayList<>();
        this.prescribedTests = new ArrayList<>();
        
        // Check if procedure has any prescriptions
        if (procedure.getPrescription() == null || procedure.getPrescription().isEmpty()) {
            context.addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_INFO, 
                "No prescription found for procedure ID: " + procedure.getProcedureId(), 
                null));
            return null; // Stay on current page
        }
        
        // Load prescription details
        for (Prescription p : procedure.getPrescription()) {
            Prescription detailed = medicalHistoryDao.getPrescriptionWithDetails(p.getPrescriptionId());
            
            if (detailed != null) {
                this.prescriptionList.add(detailed);
                
                if (detailed.getPrescribedMedicines() != null) {
                    this.prescribedMedicines.addAll(detailed.getPrescribedMedicines());
                }
                if (detailed.getTests() != null) {
                    this.prescribedTests.addAll(detailed.getTests());
                }
            }
        }
        
        resetPrescriptionPagination();
        return "Prescription.jsf?faces-redirect=true";
    }
    
    public String sortMedicinesBy(String column) {
        if (column.equals(this.medicineSortColumn)) {
            this.medicineSortAscending = !this.medicineSortAscending;
        } else {
            this.medicineSortColumn = column;
            this.medicineSortAscending = true;
        }

        if (prescribedMedicines != null) {
            Collections.sort(prescribedMedicines, new Comparator<PrescribedMedicines>() {
                public int compare(PrescribedMedicines m1, PrescribedMedicines m2) {
                    int result = 0;
                    switch (column) {
                        case "prescribedId":
                            result = safeCompare(m1.getPrescribedId(), m2.getPrescribedId());
                            break;
                        case "medicineName":
                            result = safeCompare(m1.getMedicineName(), m2.getMedicineName());
                            break;
                        case "dosage":
                            result = safeCompare(m1.getDosage(), m2.getDosage());
                            break;
                        case "type":
                            String type1 = m1.getType() != null ? m1.getType().toString() : "";
                            String type2 = m2.getType() != null ? m2.getType().toString() : "";
                            result = type1.compareToIgnoreCase(type2);
                            break;
                        case "duration":
                            result = safeCompare(m1.getDuration(), m2.getDuration());
                            break;
                        case "startDate":
                            result = safeCompare(m1.getStartDate(), m2.getStartDate());
                            break;
                        case "endDate":
                            result = safeCompare(m1.getEndDate(), m2.getEndDate());
                            break;
                        default:
                            result = 0;
                    }
                    return medicineSortAscending ? result : -result;
                }

                private <T extends Comparable<T>> int safeCompare(T o1, T o2) {
                    if (o1 == null && o2 == null) return 0;
                    if (o1 == null) return -1;
                    if (o2 == null) return 1;
                    return o1.compareTo(o2);
                }
            });
        }

        medicineCurrentPage = 1;
        getPaginatedMedicines();
        return null;
    }



    public String viewMedicinesForSelectedPrescription(Prescription prescription) {
        FacesContext context = FacesContext.getCurrentInstance();
    	
        this.selectedPrescription = prescription;
        this.allMedicines = new ArrayList<>(selectedPrescription.getPrescribedMedicines());
        this.medicineCurrentPage = 1;
        this.medicineSortColumn = "";
        this.medicineSortAscending = true;
        
        if (allMedicines == null || allMedicines.isEmpty()) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "No medicines found for this prescription.", null));
        }
        
        return "PrescribedMedicines?faces-redirect=true";
    }

    public int getTotalMedicineRecords() {
        return (allMedicines != null) ? allMedicines.size() : 0;
    }
    
    public int getMedicineTotalPages() {
        if (prescribedMedicines == null || prescribedMedicines.isEmpty()) {
            return 1;
        }
        return (int) Math.ceil((double) prescribedMedicines.size() / medicinePageSize);
    }

    public List<PrescribedMedicines> getPaginatedMedicines() {
        int startIndex = (medicineCurrentPage - 1) * medicinePageSize;
        int endIndex = Math.min(startIndex + medicinePageSize, allMedicines.size());
        return allMedicines.subList(startIndex, endIndex);
    }

    public String nextMedicinePage() {
        if (medicineCurrentPage < getTotalMedicinePages()) {
            medicineCurrentPage++;
        }
        return null;
    }

    public boolean isMedicineNextDisabled() {
        return medicineCurrentPage >= getTotalMedicinePages();
    }

    public boolean isMedicinePreviousDisabled() {
        return medicineCurrentPage <= 1;
    }

    public int getMedicineCurrentPage() {
        return medicineCurrentPage;
    }

    public int getTotalMedicinePages() {
        return (int) Math.ceil((double) allMedicines.size() / medicinePageSize);
    }

    public String previousMedicinePage() {
        if (medicineCurrentPage > 1) {
            medicineCurrentPage--;
        }
        return null;
    }

    public void goToFirstMedicinePage() {
        medicineCurrentPage = 1;
    }

    public void goToLastMedicinePage() {
        medicineCurrentPage = getTotalMedicinePages();
    }

    
    public String sortTestsBy(String column) {
        if (column.equals(this.testsSortColumn)) {
            this.testsSortAscending = !this.testsSortAscending;
        } else {
            this.testsSortColumn = column;
            this.testsSortAscending = true;
        }

        if (prescribedTests != null) {
            Collections.sort(prescribedTests, new Comparator<ProcedureTest>() {
                public int compare(ProcedureTest t1, ProcedureTest t2) {
                    int result = 0;
                    switch (column) {
                        case "testId":
                            result = safeCompare(t1.getTestId(), t2.getTestId());
                            break;
                        case "testName":
                            result = safeCompare(t1.getTestName(), t2.getTestName());
                            break;
                        case "testDate":
                            result = safeCompare(t1.getTestDate(), t2.getTestDate());
                            break;
                        case "resultSummary":
                            result = safeCompare(t1.getResultSummary(), t2.getResultSummary());
                            break;
                        default:
                            result = 0;
                    }
                    return testsSortAscending ? result : -result;
                }

                private <T extends Comparable<T>> int safeCompare(T o1, T o2) {
                    if (o1 == null && o2 == null) return 0;
                    if (o1 == null) return -1;
                    if (o2 == null) return 1;
                    return o1.compareTo(o2);
                }
            });
        }

        // Reset pagination to page 1
        testCurrentPage = 1;
        getPaginatedTests();
        return null;
    }


    public String viewTestsForSelectedPrescription(Prescription prescription) {
        FacesContext context = FacesContext.getCurrentInstance();

        this.selectedPrescription = prescription;
        
        if (selectedPrescription.getTests() == null || selectedPrescription.getTests().isEmpty()) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "No tests found for this prescription.", null));
        }
        
        return "ProcedureTest?faces-redirect=true";
    }

    
    
    public List<ProcedureTest> getPaginatedTests() {
        int start = (testCurrentPage - 1) * testPageSize;
        int end = Math.min(start + testPageSize, prescribedTests.size());
        return prescribedTests.subList(start, end);
    }

    public int getTotalTestRecords() {
        return (prescribedTests != null) ? prescribedTests.size() : 0;
    }
    
    public int getTotalTestPages() {
        return (int) Math.ceil((double) prescribedTests.size() / testPageSize);
    }

    public void goToFirstTestPage() {
        testCurrentPage = 1;
        getPaginatedTests();
    }

    public void goToLastTestPage() {
        testCurrentPage = getTotalTestPages();
        getPaginatedTests();
    }
    public String nextTestPage() {
        if (testCurrentPage < getTotalTestPages()) testCurrentPage++;
        return null;
    }

    public String previousTestPage() {
        if (testCurrentPage > 1) testCurrentPage--;
        return null;
    }

    public boolean isTestNextDisabled() {
        return testCurrentPage >= getTotalTestPages();
    }

    public boolean isTestPreviousDisabled() {
        return testCurrentPage <= 1;
    }

    public String sortLogsBy(String column) {
        if (column.equals(this.logSortColumn)) {
            this.logSortAscending = !this.logSortAscending;
        } else {
            this.logSortColumn = column;
            this.logSortAscending = true;
        }

        if (allLogs != null) {
            Collections.sort(allLogs, new Comparator<ProcedureDailyLog>() {
                public int compare(ProcedureDailyLog l1, ProcedureDailyLog l2) {
                    int result = 0;
                    switch (column) {
                        case "logDate":
                            result = safeCompare(l1.getLogDate(), l2.getLogDate());
                            break;
                        case "vitals":
                            result = safeCompare(l1.getVitals(), l2.getVitals());
                            break;
                        case "notes":
                            result = safeCompare(l1.getNotes(), l2.getNotes());
                            break;
                        default:
                            result = 0;
                    }
                    return logSortAscending ? result : -result;
                }

                private <T extends Comparable<T>> int safeCompare(T o1, T o2) {
                    if (o1 == null && o2 == null) return 0;
                    if (o1 == null) return -1;
                    if (o2 == null) return 1;
                    return o1.compareTo(o2);
                }
            });
        }

        // Reset to first page and update pagination
        logCurrentPage = 1;
        getPaginatedLogs();
        System.out.println(getPaginatedLogs());
        return null;
    }

    public String loadProcedureLogs(String procedureId) {
        FacesContext context = FacesContext.getCurrentInstance();

        this.medicalProcedure = medicalHistoryDao.getProcedureWithLogs(procedureId);
        
        if (this.medicalProcedure != null && this.medicalProcedure.getLogs() != null) {
            this.allLogs = new ArrayList<>(this.medicalProcedure.getLogs());
        } else {
            this.allLogs = new ArrayList<>();
        }

        if (allLogs.isEmpty()) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "No logs found for this procedure.", null));
        }
        
        this.logCurrentPage = 1;
        this.logSortColumn = "";
        this.logSortAscending = true;

        getPaginatedLogs();
        System.out.println(getPaginatedLogs());

        
        return "ProcedureDailyLog.jsf?faces-redirect=true";
    }

    public int getTotalLogRecords() {
        return (allLogs != null) ? allLogs.size() : 0;
    }
    
    public int getTotalLogPages() {
        return (int) Math.ceil((double) allLogs.size() / logPageSize);
    }

    public void goToFirstLogPage() {
        logCurrentPage = 1;
        getPaginatedLogs();
    }

    public void goToLastLogPage() {
        logCurrentPage = getTotalLogPages();
        getPaginatedLogs();
    }

    public void goToNextLogPage() {
        if (logCurrentPage < getTotalLogPages()) {
            logCurrentPage++;
            getPaginatedLogs();
        }
    }

    public void goToPreviousLogPage() {
        if (logCurrentPage > 1) {
            logCurrentPage--;
            getPaginatedLogs();
        }
    }

    public boolean isLogsNextDisabled() {
        return logCurrentPage >= getTotalLogPages();
    }

    public boolean isLogsPreviousDisabled() {
        return logCurrentPage <= 1;
    }
    
    public List<ProcedureDailyLog> getPaginatedLogs() {
        int start = (logCurrentPage - 1) * logPageSize;
        int end = Math.min(start + logPageSize, allLogs.size());
        return allLogs.subList(start, end);
    }

    public String sortPrescriptionsBy(String column) {
        if (column.equals(this.prescriptionSortColumn)) {
            this.prescriptionSortAscending = !this.prescriptionSortAscending;
        } else {
            this.prescriptionSortColumn = column;
            this.prescriptionSortAscending = true;
        }

        if (allPrescriptions != null && !allPrescriptions.isEmpty()) {
            Comparator<Prescription> comparator = null;

            switch (column) {
                case "writtenOn":
                    comparator = Comparator.comparing(Prescription::getWrittenOn, Comparator.nullsLast(Date::compareTo));
                    break;
                case "startDate":
                    comparator = Comparator.comparing(Prescription::getStartDate, Comparator.nullsLast(Date::compareTo));
                    break;
                case "endDate":
                    comparator = Comparator.comparing(Prescription::getEndDate, Comparator.nullsLast(Date::compareTo));
                    break;
                case "prescriptionId":
                    comparator = Comparator.comparing(Prescription::getPrescriptionId, Comparator.nullsLast(String::compareToIgnoreCase));
                    break;
            }

            if (comparator != null) {
                if (!prescriptionSortAscending) {
                    comparator = comparator.reversed();
                }
                allPrescriptions.sort(comparator);
            }
        }

        prescriptionCurrentPage = 1;
        getPaginatedPrescriptions();
        return null;
    }


    public List<Prescription> getPaginatedPrescriptions() {
        if (prescriptionList == null || prescriptionList.isEmpty()) {
            return Collections.emptyList();
        }

        int totalSize = prescriptionList.size();
        int start = (prescriptionCurrentPage - 1) * prescriptionPageSize;
        int end = Math.min(start + prescriptionPageSize, totalSize);

        if (start >= totalSize || start < 0 || end < 0 || start > end) {
            return Collections.emptyList();
        }

        return prescriptionList.subList(start, end);
    }

    public int getTotalPrescriptionRecords() {
        return (prescriptionList != null) ? prescriptionList.size() : 0;
    }
    
    public int getTotalPrescriptionPages() {
        return (prescriptionList == null || prescriptionList.isEmpty()) ? 1 :
                (int) Math.ceil((double) prescriptionList.size() / prescriptionPageSize);
    }

    public void goToFirstPrescriptionPage() {
        prescriptionCurrentPage = 1;
        getPaginatedPrescriptions();
    }

    public void goToLastPrescriptionPage() {
        prescriptionCurrentPage = getPrescriptionTotalPages();
        getPaginatedPrescriptions();
    }

    public void goToNextPrescriptionPage() {
        if (prescriptionCurrentPage < getPrescriptionTotalPages()) {
            prescriptionCurrentPage++;
            getPaginatedPrescriptions();
        }
    }

    public void goToPreviousPrescriptionPage() {
        if (prescriptionCurrentPage > 1) {
            prescriptionCurrentPage--;
            getPaginatedPrescriptions();
        }
    }

    public boolean isPrescriptionNextDisabled() {
        return prescriptionCurrentPage >= getTotalPrescriptionPages();
    }

    public boolean isPrescriptionPreviousDisabled() {
        return prescriptionCurrentPage <= 1;
    }

    public void resetPrescriptionPagination() {
        prescriptionCurrentPage = 1;
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

	public String getProcedureTypeSelected() {
		return procedureTypeSelected;
	}

	public void setProcedureTypeSelected(String procedureTypeSelected) {
		this.procedureTypeSelected = procedureTypeSelected;
	}

	public List<PrescribedMedicines> getPrescribedMedicines() {
		return prescribedMedicines;
	}

	public void setPrescribedMedicines(List<PrescribedMedicines> prescribedMedicines) {
		this.prescribedMedicines = prescribedMedicines;
	}

	public List<ProcedureTest> getPrescribedTests() {
		return prescribedTests;
	}

	public void setPrescribedTests(List<ProcedureTest> prescribedTests) {
		this.prescribedTests = prescribedTests;
	}

	
	public Prescription getSelectedPrescription() {
		return selectedPrescription;
	}

	public void setSelectedPrescription(Prescription selectedPrescription) {
	    this.selectedPrescription = selectedPrescription;
	    if (selectedPrescription != null && selectedPrescription.getPrescribedMedicines() != null) {
	        this.allMedicines = new ArrayList<>(selectedPrescription.getPrescribedMedicines());
	        this.medicineCurrentPage = 1; // Reset to first page
	    } else {
	        this.allMedicines = new ArrayList<>();
	    }
	}

	public List<PrescribedMedicines> getMedicinesForSelectedPrescription() {
	    return getPaginatedMedicines();
	}

	public String getMedicineSortColumn() {
		return medicineSortColumn;
	}

	public void setMedicineSortColumn(String medicineSortColumn) {
		this.medicineSortColumn = medicineSortColumn;
	}

	public boolean isMedicineSortAscending() {
		return medicineSortAscending;
	}

	public void setMedicineSortAscending(boolean medicineSortAscending) {
		this.medicineSortAscending = medicineSortAscending;
	}

	public String getTestsSortColumn() {
		return testsSortColumn;
	}

	public void setTestsSortColumn(String testsSortColumn) {
		this.testsSortColumn = testsSortColumn;
	}

	public boolean isTestsSortAscending() {
		return testsSortAscending;
	}

	public void setTestsSortAscending(boolean testsSortAscending) {
		this.testsSortAscending = testsSortAscending;
	}

	public List<ProcedureDailyLog> getAllLogs() {
	    return allLogs;
	}

	public void setAllLogs(List<ProcedureDailyLog> allLogs) {
	    this.allLogs = allLogs;
	}


	public int getLogCurrentPage() {
	    return logCurrentPage;
	}

	public String getLogSortColumn() {
	    return logSortColumn;
	}

	public boolean isLogSortAscending() {
	    return logSortAscending;
	}

	public void setLogSortColumn(String logSortColumn) {
	    this.logSortColumn = logSortColumn;
	}

	public void setLogSortAscending(boolean logSortAscending) {
	    this.logSortAscending = logSortAscending;
	}

	public List<Prescription> getAllPrescriptions() {
		return allPrescriptions;
	}

	public void setAllPrescriptions(List<Prescription> allPrescriptions) {
		this.allPrescriptions = allPrescriptions;
	}

	public int getPrescriptionTotalPages() {
		return prescriptionTotalPages;
	}

	public void setPrescriptionTotalPages(int prescriptionTotalPages) {
		this.prescriptionTotalPages = prescriptionTotalPages;
	}

	public List<PrescribedMedicines> getAllMedicines() {
		return allMedicines;
	}

	public void setAllMedicines(List<PrescribedMedicines> allMedicines) {
		this.allMedicines = allMedicines;
	}

	public int getMedicinePageSize() {
		return medicinePageSize;
	}

	public void setMedicinePageSize(int medicinePageSize) {
		this.medicinePageSize = medicinePageSize;
	}

	public int getTestCurrentPage() {
		return testCurrentPage;
	}

	public void setTestCurrentPage(int testCurrentPage) {
		this.testCurrentPage = testCurrentPage;
	}

	public int getTestPageSize() {
		return testPageSize;
	}

	public void setTestPageSize(int testPageSize) {
		this.testPageSize = testPageSize;
	}

	public int getLogPageSize() {
		return logPageSize;
	}

	public void setLogPageSize(int logPageSize) {
		this.logPageSize = logPageSize;
	}

	public void setMedicineCurrentPage(int medicineCurrentPage) {
		this.medicineCurrentPage = medicineCurrentPage;
	}

	public void setLogCurrentPage(int logCurrentPage) {
		this.logCurrentPage = logCurrentPage;
	}
}
