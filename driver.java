import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.Scanner;
import java.io.FileReader;
import java.io.File;
import java.io.FileNotFoundException;

public class driver {

    public static void main(String[] args) {
        // key          -> value
        // chromosome   -> List<SNP>
        Map<Integer, List<SNP>> map = new TreeMap<>();

        try (Scanner file = new Scanner(new FileReader(new File("AncestryDNA.txt")))) {
            int lineNumber = 1;
            while (file.hasNext()) {
                String line = file.nextLine();
                if (line.startsWith("#") || line.length() == 0 || line.matches("^\\s*$"))
                    continue; //skip empty or commented lines

                String[] arr = line.split("\t");

                //Expected column header:
                //rsid    chromosome      position        allele1 allele2
                String  chromosome  = arr[1],
                        allele1     = arr[3],
                        allele2     = arr[4],
                        position    = arr[2],
                        rsid        = arr[0];

                SNP snp = null;
                try {
                    snp = new SNP(chromosome, allele1, allele2, position, rsid);

                    Integer chromosomeKey = Integer.parseInt(chromosome);
                    List<SNP> chromosomeList;
                    if (map.containsKey(chromosomeKey)) {
                        chromosomeList = map.get(chromosomeKey);
                    } else {
                        chromosomeList = new ArrayList<>();
                    }
                    chromosomeList.add(snp);
                    map.put(chromosomeKey, chromosomeList);
                } catch (NumberFormatException ex) {
                    //TODO: Find a better way to exclude the header columns.
                    //System.err.println("Error processing: " + Arrays.toString(arr));
                    continue;
                }

                lineNumber++;
            }
        } catch (FileNotFoundException ex) {
            System.err.println(ex.getMessage());
        } 




        // How many basepairs were identified in each chromosome?
        System.out.println("How many basepairs were identified in each chromosome?");
        queryBasepairsPerChromosome(map);

        // Which chromosome has the most, and least basepairs?
        System.out.println("\n\nWhich chromosome has the most, and least basepairs?");
        queryMostLeastBasepairs(map);

        // On average, how many basepairs does a chromosome have?
        System.out.println("\n\nWhich chromosome has the most, and least basepairs?");
        queryAvgBasepairs(map);
    }

    private static void queryBasepairsPerChromosome(Map<Integer, List<SNP>> map) {
        for (Integer key : map.keySet()) {
            List<SNP> chromosomeList = map.get(key);
            System.out.println("\tchromosome " + key + " has " + chromosomeList.size() + " basepairs.");
        }
    }

    private static void queryMostLeastBasepairs(Map<Integer, List<SNP>> map) {
        final int DEFAULT = 0;

        List<Integer> numBasepairsList = new ArrayList<>();
        int mostBasepairs = Integer.MIN_VALUE,
            leastBasepairs = Integer.MAX_VALUE,
            chromosomeWithMostBasepairs = DEFAULT,
            chromosomeWithLeastBasepairs = DEFAULT;

        for (Integer chromosomeNumber : map.keySet()) {
            List<SNP> chromosomeList = map.get(chromosomeNumber);
            int size = chromosomeList.size();
            if (mostBasepairs < size) {
                mostBasepairs = size;
                chromosomeWithMostBasepairs = chromosomeNumber;
            }
            if (leastBasepairs > size) {
                leastBasepairs = size;
                chromosomeWithLeastBasepairs = chromosomeNumber;
            }
        }
        System.out.println("\tChromosome " + chromosomeWithMostBasepairs + " has the most basepairs at: " + mostBasepairs + ".");
        System.out.println("\tChromosome " + chromosomeWithLeastBasepairs + " has the least basepairs at: " + leastBasepairs + ".");
    }

    private static void queryAvgBasepairs(Map<Integer, List<SNP>> map) {
        List<Integer> numBasepairsList = new ArrayList<>();
        int sum = 0;
        for (Integer key : map.keySet()) {
            List<SNP> chromosomeList = map.get(key);
            int numBasepairs = chromosomeList.size();
            numBasepairsList.add(numBasepairs);
            sum += numBasepairs;
        }
        System.out.println("\tOn average, a chromosome has " + sum/numBasepairsList.size() + " basepairs.");
    }
}//class driver

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
