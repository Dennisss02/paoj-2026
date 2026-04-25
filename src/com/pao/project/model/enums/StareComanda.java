package com.pao.project.model.enums;

public enum StareComanda {
    INITIATA {
        @Override
        public boolean stareFinala() {
            return false;
        }
    },
    PLASATA {
        @Override
        public boolean stareFinala() {
            return false;
        }
    },
    FINALIZATA {
        @Override
        public boolean stareFinala() {
            return true;
        }
    },
    ANULATA {
        @Override
        public boolean stareFinala() {
            return true;
        }
    };

    public abstract boolean stareFinala();
}
