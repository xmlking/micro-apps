package micro.apps.service

class ScoresNotAvailableException : Exception {
    constructor() {}
    constructor(string: String?) : super(string) {}
    constructor(string: String?, thrwbl: Throwable?) : super(string, thrwbl) {}
    constructor(thrwbl: Throwable?) : super(thrwbl) {}
    constructor(string: String?, thrwbl: Throwable?, bln: Boolean, bln1: Boolean) : super(string, thrwbl, bln, bln1) {}
}
