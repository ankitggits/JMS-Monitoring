var ServerEnv = {
    LOCAL : "http://localhost",
    POC : "http://localhost",
    DEV:"http://localhost",
    STEST:"http://localhost"
};

var Port = {
    PAYMENT : ':8080',
    CUSTOMER: ':8081',
    MERCHANT: ':8082'
};

var DomainURL = {
    PAYMENT : '/test-payment',
    CUSTOMER: '/test-customer',
    MERCHANT: '/test-merchant'
};

var PingURL = {
    PAYMENT : '/ping-payment',
    CUSTOMER: '/ping-customer',
    MERCHANT: '/ping-merchant'
};

var ListenerURL = '/listener/concurrency';
    