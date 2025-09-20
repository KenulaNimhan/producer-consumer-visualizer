package site.visualizer.config;

public class Configuration {
    private int
            totalNoOfTickets,
            bufferCap,
            vendorCount,
            customerCount,
            capPerConsumer,
            releaseRate,
            retrievalRate;

    public Configuration(){}

    // GETTERS

    public int getTotalNoOfTickets() {
        return totalNoOfTickets;
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

    public void setBufferCap(int bufferCap) {
        this.bufferCap = bufferCap;
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

    // HELPER METHOD
    public boolean isValid() {
        return (customerCount*capPerConsumer) <= totalNoOfTickets;
    }
}
