package site.visualizer.config;

public class Configuration {
    private int
            totalNoOfTickets,
            vendorCount,
            customerCount,
            capPerConsumer,
            releaseRate,
            retrievalRate;

    Configuration(){}

    // GETTERS

    public int getTotalNoOfTickets() {
        return totalNoOfTickets;
    }

    public int getVendorCount() {
        return vendorCount;
    }

    public int getCustomerCount() {
        return customerCount;
    }

    public int getCapPerConsumer() {
        return capPerConsumer;
    }

    public int getReleaseRate() {
        return releaseRate;
    }

    public int getRetrievalRate() {
        return retrievalRate;
    }

    // SETTERS

    public void setTotalNoOfTickets(int totalNoOfTickets) {
        this.totalNoOfTickets = totalNoOfTickets;
    }

    public void setVendorCount(int vendorCount) {
        this.vendorCount = vendorCount;
    }

    public void setCustomerCount(int customerCount) {
        this.customerCount = customerCount;
    }

    public void setCapPerConsumer(int capPerConsumer) {
        this.capPerConsumer = capPerConsumer;
    }

    public void setReleaseRate(int releaseRate) {
        this.releaseRate = releaseRate;
    }

    public void setRetrievalRate(int retrievalRate) {
        this.retrievalRate = retrievalRate;
    }
}
