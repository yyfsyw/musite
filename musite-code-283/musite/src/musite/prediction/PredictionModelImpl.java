/**
 * Musite
 * Copyright (C) 2010 Digital Biology Laboratory, University Of Missouri
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package musite.prediction;

import java.io.Serializable;

import java.util.Date;
import java.util.Set;
import java.util.Properties;

import musite.prediction.classifier.BinaryClassifier;

import musite.PTM;

import musite.util.AminoAcid;

/**
 *
 * @author Jianjiong Gao
 */
public class PredictionModelImpl implements PredictionModel, Serializable {
    private static final long serialVersionUID = 2018923708870026562L;

    protected PTM ptm;
    protected Set<AminoAcid> aminoAcids;
    protected BinaryClassifier classifier;
    protected Properties props;
    protected SpecificityEstimator specEstimator;
    protected String name;
    protected String comment;
    protected Date date;
    protected transient String file;
    
    public static class Builder {
        private BinaryClassifier classifier = null;
        private PTM ptm = null;
        private Set<AminoAcid> aminoAcids = null;
        private Properties props = null;
        private SpecificityEstimator specEstimator = null;
        private String name = null;
        private String comment = null;

        public Builder classifier(BinaryClassifier classifier) {
            this.classifier = classifier;
            return this;
        }

        public Builder ptm(PTM ptm) {
            this.ptm = ptm;
            return this;
        }

        public Builder aminoAcids(Set<AminoAcid> aminoAcids) {
            this.aminoAcids = aminoAcids;
            return this;
        }

        public Builder props(Properties props) {
            this.props = props;
            return this;
        }

        public Builder specEstimator(SpecificityEstimator specEstimator) {
            this.specEstimator = specEstimator;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder comment(String comment) {
            this.comment = comment;
            return this;
        }

        public PredictionModelImpl build() {
            return new PredictionModelImpl(this);
        }

    }

    public PredictionModelImpl(PredictionModel model) {
        if (model==null)
            throw new IllegalArgumentException();
        this.classifier = model.getClassifier();
        this.ptm = model.getSupportedPTM();
        this.aminoAcids = model.getSupportedAminoAcid();
        this.props = model.getModelProperties();
        this.specEstimator = model.getSpecEstimator();
        this.name = model.getName();
        this.comment = model.getComment();
        this.file = model.getFile();
        this.date = model.getTimeStamp();
    }

    protected PredictionModelImpl(Builder builder) {
        this.classifier = builder.classifier;
        this.ptm = builder.ptm;
        this.aminoAcids = builder.aminoAcids;
        this.props = builder.props;
        this.specEstimator = builder.specEstimator;
        this.name = builder.name;
        this.comment = builder.comment;
        this.file = null;
        this.date = new Date();
    }

    public BinaryClassifier getClassifier() {
        return classifier;
    }

    public Properties getModelProperties() {
        return props;
    }

    public Set<AminoAcid> getSupportedAminoAcid() {
        return aminoAcids;
    }

    public SpecificityEstimator getSpecEstimator() {
        return specEstimator;
    }

    public PTM getSupportedPTM() {
        return ptm;
    }

    public String getName() {
        return name;
    }

    public String getComment() {
        return comment;
    }

    public String getFile() {
        return file;
    }

    public Date getTimeStamp() {
        return date;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setComment(final String comment) {
        this.comment = comment;
    }

    public void setFile(final String file) {
        this.file = file;
    }

    public String toString() {
        return getName();
    }
}
