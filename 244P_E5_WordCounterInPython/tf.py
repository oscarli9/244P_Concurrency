import re, collections, glob
from multiprocessing.dummy import Pool


class WordCounter:

    def main(self):
        stopwords = set(open('stop_words').read().split(','))
        files = glob.glob("*.txt")

        def counter(filename):
            words = re.findall('\w{3,}', open(filename).read().lower())
            self.counts += collections.Counter(w for w in words if w not in stopwords)

        self.counts = collections.Counter()
        p = Pool(4)
        p.map(counter, files)
        p.close()
        p.join()

        for (w, c) in self.counts.most_common(40):
            print(w, '-', c)


if __name__ == "__main__":
    tf = WordCounter()
    tf.main()
