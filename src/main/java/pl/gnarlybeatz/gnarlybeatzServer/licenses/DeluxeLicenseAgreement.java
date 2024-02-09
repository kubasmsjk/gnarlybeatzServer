package pl.gnarlybeatz.gnarlybeatzServer.licenses;

import java.io.File;

public class DeluxeLicenseAgreement extends LicenseAgreement {
    private static final String mechanicalRights = "Mechanical Rights. The Licensor hereby grants to Licensee a non-exclusive license to use Master Recording in the reproduction, duplication, manufacture, and distribution of phonograph records, cassette tapes, compact disk, digital downloads, other miscellaneous audio and digital recordings, and any lifts and versions thereof (collectively, the \"Recordings\", and individually, a \"Recordings\") worldwide for up to the pressing or selling a total of UNLIMITED copies of such Recordings or any combination of such Recordings, condition upon the payment to the Licensor a sum of $20.00 US dollars, receipt of which is confirmed.\n\n";
    private static final String synchronizationRights = "Synchronization Rights. The Licensor hereby grants limited synchronization rights for one (1) music video streamed online (Youtube, Vimeo, etc..) for up to UNLIMITED streams total on all websites. A separate synchronization license will need to be purchased for distribution of video to Television, Film or Video game.\n\n";
    private static final String broadcastRights = "Broadcast Rights. The Licensor hereby grants to Licensee broadcasting rights up to Unlimited Radio Stations.\n\n";
    private static final String consideration = "Consideration. In consideration for the rights granted under this agreement, Licensee shall pay to licensor the sum of $20.00 US dollars and other good and valuable consideration, payable to \"xGnarly\", receipt of which is hereby acknowledged. If the Licensee fails to account to the Licensor, timely complete the payments provided for hereunder, or perform its other obligations hereunder, including having insufficient bank balance, the licensor shall have the right to terminate License upon written notice to the Licensee. Such termination shall render the recording, manufacture and/or distribution of Recordings for which monies have not been paid subject to and actionable infringements under applicable law, including, without limitation, the United States Copyright Act, as amended.\n\n";

    public static File generatePDF(String licenseeName, String artistName, String address, String beatName) {
        return LicenseAgreement.generatePDF(licenseeName, artistName, address, beatName, mechanicalRights, synchronizationRights, broadcastRights, consideration);
    }
}
