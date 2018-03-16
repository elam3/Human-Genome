import java.io.Serializable;

// Single Nucleotide Polymorphism (SNP)
public class SNP implements Comparable<SNP>, Serializable {
    private int     rsid,
                    chromosome,
                    position;
    private String  allele1, //Possible observations:
                    allele2; // A, C, G, T, I, D, 0; FAQ below for more details

    public SNP(String chromosome, String allele1, String allele2, String position, String rsid) {
        this.chromosome = Integer.parseInt(chromosome);
        this.allele1    = allele1;
        this.allele2    = allele2;
        this.position   = Integer.parseInt(position);
        
        if (rsid.startsWith("rs")) {
            //trim the rs- prefix; e.g. rs123 -> 123
            this.rsid       = Integer.parseInt(rsid.substring(2));
        } else throw new IllegalArgumentException();
    }

    // Getters
    public int      getChromosome() { return chromosome; }
    public String   getAllele1()    { return allele1; }
    public String   getAllele2()    { return allele2; }
    public int      getPosition()   { return position; }
    public int      getRSID()       { return rsid; }

    // Setters
    // None available

    @Override public String toString() { return "SNP -> [" + chromosome + ": " + allele1 + ", " + allele2 + ", " + position + ", " + rsid + "]"; }

    @Override public boolean equals(Object obj) {
        if (obj instanceof SNP) {
            SNP other = (SNP) obj;
            return this.chromosome == other.chromosome
                && this.allele1.equals(other.allele1)
                && this.allele2.equals(other.allele2)
                && this.position == other.position
                && this.rsid == other.rsid;
        } else return false;
    }

    @Override public int compareTo(SNP other) {
        // Primary Sort: chromosome
        if (this.chromosome == other.chromosome) {
            
            // Secondary Sort: position
            if (this.position == other.position) {
                
                // Tertiary Sort: rsid
                return Integer.compare(this.rsid, other.rsid);

            } else return Integer.compare(this.position, other.position);

        } else return Integer.compare(this.chromosome, other.chromosome);
    }//compareTo
}//class SNP

/*
 * Source: https://www.ancestry.com/dna/en/legal/us/faq
 *
 * Quote:
 * 6. How do I read my raw DNA data?
 *
 * Raw DNA data is provided in a TAB delimited text file. This file contains a
 * header describing the data and five columns of information. Each line
 * corresponds to one single nucleotide polymorphism (SNP) or indel. A SNP is a
 * single site in the genome that is known to vary across individuals. An indel
 * is either an insert or a deletion site. Our genotyping assay measures what
 * nucleotide is present at each of approximately 700,000 variants on both the
 * paternal and maternal copies of the genome. The possible observations are A
 * for adenine, C for cytosine, G for guanine, T for thymine, I for insertion’
 * and ‘D for deletion, or 0 for missing data. Column one provides the identifier
 * (including the #rsID where possible). Columns two and three contain the
 * chromosome and basepair position of the variant using human reference 37.1
 * coordinates. Columns four and five contain the two alleles observed at this
 * variant (genotype). The specific letters present are called alleles
 * and the pair of alleles observed at a variant is called the genotype. Every
 * variant we examine is bi-allelic, meaning there are only two possible alleles
 * present. This means that there are three possible genotypes at each variant.
 * For example, if a variant contains either C (cytosine) or A (adenine), then
 * the possible genotypes are C C, A A, or A C (allele order is irrelevant so C
 * A is the same as A C). Each chromosome is composed of two complementary
 * strands, often called forward and reverse, and alleles may be reported on
 * either strand. For example, a SNP genotype that is G G on the forward strand
 * will be C C on the reverse strand. Likewise G A on the forward strand is C T
 * on the reverse strand. It is essential to know the strand on which your data
 * is reported. For the AncestryDNA product, we report data for the SNPs on the
 * forward strand with respect to the human reference genome.
 */
