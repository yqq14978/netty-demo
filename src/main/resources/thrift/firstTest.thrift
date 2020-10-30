namespace java com.yqq.nettydemo.thrift.entity

typedef i16 short
typedef i32 int
typedef i64 long
typedef bool boolean
typedef string String

struct Person {
    1: optional String name,
    2: optional int age,
    3: optional boolean sex
}

exception PersonException {
    1: optional int code,
    2: optional String msg
}

service PersonService {
    boolean isAlive (1: required String name),
    void savePerson (1: required Person person) throws (1: PersonException personException)
}