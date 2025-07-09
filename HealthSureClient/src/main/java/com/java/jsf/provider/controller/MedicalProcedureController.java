package com.java.jsf.provider.controller;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import com.java.jsf.provider.dao.MedicalProcedureDao;
import com.java.jsf.provider.model.MedicalProcedure;

public class MedicalProcedureController {

    private String searchKey;
    private List<MedicalProcedure> searchResults;
    private MedicalProcedureDao medicalProcedureDao;
    private MedicalProcedure medicalProcedure;

    private String searchType = "";
    private String nameSearchMode = "";

    private String sortColumn = "";
    private boolean sortAscending = true;

    private int currentPage = 0;
    private int pageSize = 5;

    public String confirmSearchType() {
        if (!"name".equals(this.searchType)) {
            this.nameSearchMode = ""; // clear if switched from 'name'
        }
        return null;
    }

    public String searchProcedures() {
        FacesContext context = FacesContext.getCurrentInstance();

        if (searchType == null || searchType.trim().isEmpty()) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please select a search type.", null));
            return null;
        }

        if (searchKey == null || searchKey.trim().isEmpty()) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Search value cannot be empty.", null));
            return null;
        }

        String trimmedKey = searchKey.trim();

        switch (searchType) {
            case "hid":
                if (!trimmedKey.matches("^HID\\d{3}$")) {
                    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "HID must be in format HID001.", null));
                    return null;
                }
                searchResults = medicalProcedureDao.searchByHid(trimmedKey);
                break;

            case "name":
                if (nameSearchMode == null || nameSearchMode.trim().isEmpty()) {
                    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please select Name search mode (StartsWith or Contains).", null));
                    return null;
                }
                if ("startsWith".equals(nameSearchMode)) {
                    searchResults = medicalProcedureDao.searchByNameStartsWith(trimmedKey);
                } else if ("contains".equals(nameSearchMode)) {
                    searchResults = medicalProcedureDao.searchByNameContains(trimmedKey);
                }
                break;

            case "mobile":
                if (!trimmedKey.matches("^[6-9]\\d{9}$")) {
                    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Enter a valid 10-digit phone number.", null));
                    return null;
                }
                searchResults = medicalProcedureDao.searchByMobile(trimmedKey);
                break;

            default:
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid search type selected.", null));
                searchResults = null;
        }

        sortResults();
        resetPagination();
        return null;
    }

    public String sortBy(String column) {
        if (column.equals(this.sortColumn)) {
            this.sortAscending = !this.sortAscending;
        } else {
            this.sortColumn = column;
            this.sortAscending = true;
        }
        sortResults();
        resetPagination();
        return null;
    }

    private void sortResults() {
        if (searchResults == null) return;

        Collections.sort(searchResults, new Comparator<MedicalProcedure>() {
            public int compare(MedicalProcedure p1, MedicalProcedure p2) {
                int result = 0;
                switch (sortColumn) {
                    case "procedureId":
                        result = p1.getProcedureId().compareTo(p2.getProcedureId());
                        break;
                    case "procedureDate":
                        result = p1.getProcedureDate().compareTo(p2.getProcedureDate());
                        break;
                    case "diagnosis":
                        result = p1.getDiagnosis().compareTo(p2.getDiagnosis());
                        break;
                    case "recommendations":
                        result = p1.getRecommendations().compareTo(p2.getRecommendations());
                        break;
                    case "createdAt":
                        result = p1.getCreatedAt().compareTo(p2.getCreatedAt());
                        break;
                    case "recipientName":
                        result = (p1.getRecipient().getFirstName() + p1.getRecipient().getLastName())
                            .compareTo(p2.getRecipient().getFirstName() + p2.getRecipient().getLastName());
                        break;
                    default:
                        result = 0;
                }
                return sortAscending ? result : -result;
            }
        });
    }

    public List<MedicalProcedure> getPaginatedList() {
        if (searchResults == null) return null;

        int start = currentPage * pageSize;
        int end = Math.min(start + pageSize, searchResults.size());

        if (start >= end) {
            currentPage = 0;
            start = 0;
            end = Math.min(pageSize, searchResults.size());
        }

        return searchResults.subList(start, end);
    }

    public int getTotalPages() {
        if (searchResults == null || searchResults.isEmpty()) return 1;
        return (int) Math.ceil((double) searchResults.size() / pageSize);
    }

    public boolean isNextButtonDisabled() {
        if (searchResults == null) return true;
        return ((currentPage + 1) * pageSize) >= searchResults.size();
    }

    public boolean isPreviousButtonDisabled() {
        return currentPage == 0;
    }

    public String nextPage() {
        if (currentPage < getTotalPages() - 1) {
            currentPage++;
        }
        return null;
    }

    public String previousPage() {
        if (currentPage > 0) {
            currentPage--;
        }
        return null;
    }

    public void resetPagination() {
        currentPage = 0;
    }

    public String resetForm() {
        this.searchType = "";
        this.nameSearchMode = "";
        this.searchKey = null;
        this.searchResults = null;
        return null;
    }

    // Getters and Setters

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    public List<MedicalProcedure> getSearchResults() {
        return searchResults;
    }

    public void setSearchResults(List<MedicalProcedure> searchResults) {
        this.searchResults = searchResults;
    }

    public MedicalProcedureDao getMedicalProcedureDao() {
        return medicalProcedureDao;
    }

    public void setMedicalProcedureDao(MedicalProcedureDao medicalProcedureDao) {
        this.medicalProcedureDao = medicalProcedureDao;
    }

    public MedicalProcedure getMedicalProcedure() {
        return medicalProcedure;
    }

    public void setMedicalProcedure(MedicalProcedure medicalProcedure) {
        this.medicalProcedure = medicalProcedure;
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
}
