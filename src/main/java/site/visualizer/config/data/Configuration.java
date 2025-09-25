package site.visualizer.config.data;

public class Configuration {
    private int
            totalTickets,
            bufferCap,
            vendorCount,
            customerCount,
            capPerCustomer,
            releaseRate,
            retrievalRate;

    public Configuration(){}

    // GETTERS

    public int getTotalTickets() {
        return totalTickets;
    }

    public int getBufferCap() {
        return bufferCap;
    }

    public int getVendorCount() {
        return vendorCount;
    }

    public int getCustomerCount() {
        return customerCount;
    }

    public int getCapPerCustomer() {
        return capPerCustomer;
    }

    public int getReleaseRate() {
        return releaseRate;
    }

    public int getRetrievalRate() {
        return retrievalRate;
    }

    // SETTERS

    public void setTotalTickets(int totalTickets) {
        this.totalTickets = totalTickets;
    }

    public void setBufferCap(int bufferCap) {
        this.bufferCap = bufferCap;
    }

    public void setVendorCount(int vendorCount) {
        this.vendorCount = vendorCount;
    }

    public void setCustomerCount(int customerCount) {
        this.customerCount = customerCount;
    }

    public void setCapPerCustomer(int capPerCustomer) {
        this.capPerCustomer = capPerCustomer;
    }

    public void setReleaseRate(int releaseRate) {
        this.releaseRate = releaseRate;
    }

    public void setRetrievalRate(int retrievalRate) {
        this.retrievalRate = retrievalRate;
    }

    // HELPER METHOD
    public boolean isValid() {
        return (customerCount*capPerCustomer) <= totalTickets;
    }
}
